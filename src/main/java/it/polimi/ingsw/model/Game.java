package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.Eriantys;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.GamePhase.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

/**
 * Class containing all the game info needed to play. COntains also the reference to all players, teams, islands, clouds,
 * characters, and so on.
 */
public class Game implements Serializable {

    //Constants
    private final Integer availableCharactersNumber = 3;
    private final Integer allCharactersNumber = 12;
    private final Integer islandsNumber = 12;
    private final Integer studentNumber = 130;
    private final ArrayList<Integer> allCharacters; //All existing characters

    private Integer gameEndTimout = 10;
    private final Boolean expertMode;
    private Boolean startedTimer = false;
    private String currentPlayer;
    private Integer emptyPlayerNumber = 1;
    private final Integer playerNumber;
    private List<Island> islands;
    private final List<Cloud> clouds;
    private ArrayList<Integer> students; //This is the game bag
    private final ArrayList<Team> teams;
    private Team winnerTeam;
    private List<Player> players;
    private int turnNumber;
    private List<Player> currentTurnOrder; //contains players for the current round
    private List<Player> actionPhaseOrder; //contains players for the action phase
    private List<Player> nextTurnOrder; //contains players for the next round
    private GamePhase currentPhase = OFF; //Game phase
    private final int studentsForClouds;
    private final int studentsForBoards;
    private final int studentsToMove;
    private int movedStudentsInTurn;
    private int movedFromCloudInTurn;
    private boolean movedMotherNatureInTurn = false;

    private Character[] availableCharacters;

    //Modifiers
    private Integer[] studentValue = {1, 1, 1, 1, 1}; //defaults to 1
    private Integer towerValue = 1; //defaults to 1
    private Integer influenceModifier = 0; //defaults to 0
    private Integer motherNatureMovements = 0; //defaults to 0
    private Integer studentsInDiningModifier = 0; //defaults to 0

    /**
     * Needed for unique Piece ID generation
     */
    private static Integer nextPieceID = 0;

    /**
     * Custom constructor for small model for the view
     *
     * @param fullGame mainGame to minify
     */
    private Game(Game fullGame){

        this.expertMode = fullGame.isExpertMode();

        if(this.expertMode) {
            this.availableCharacters = fullGame.availableCharacters;
        }

        this.currentPhase = fullGame.currentPhase;
        this.currentPlayer = fullGame.currentPlayer;
        this.players = fullGame.players;
        this.playerNumber = fullGame.players.size();
        this.teams = fullGame.teams;
        this.islands = fullGame.islands;
        this.clouds = fullGame.clouds;
        this.allCharacters = null;
        this.studentsForClouds = fullGame.studentsForClouds;
        this.studentsForBoards = fullGame.studentsForBoards;
        this.studentsToMove = fullGame.studentsToMove;
        this.movedMotherNatureInTurn = fullGame.movedMotherNatureInTurn;
        this.movedFromCloudInTurn = fullGame.movedFromCloudInTurn;
        this.students = fullGame.students;
        this.turnNumber = fullGame.turnNumber;
        this.winnerTeam = fullGame.winnerTeam;

        //Modifiers
        this.studentValue = fullGame.studentValue;
        this.towerValue = fullGame.towerValue;
        this.influenceModifier = fullGame.influenceModifier;
        this.motherNatureMovements = fullGame.motherNatureMovements;
        this.studentsInDiningModifier = fullGame.studentsInDiningModifier;
    }

    /**
     * Main constructor for the game. Handles setup of modifies, islands, players, bag, clouds and all the game board.
     * Picks random characters, fills the pieces if needed, updates teams.
     *
     * @param playerNumber number of players
     * @param nicknameOfCreator nickname of the creator
     * @param expertMode boolean flag for expert mode (i.e.: characters)
     */
    public Game(int playerNumber, String nicknameOfCreator, Boolean expertMode) {

        setCurrentPhase(GamePhase.SETUP);

        //Piece values setup
        towerValue = 1;
        studentValue = new Integer[5];
        for(int i = 0; i < 5; i++){
            studentValue[i] = 1;
        }
        //Modifiers Setup
        motherNatureMovements = 0;
        influenceModifier = 0;
        studentsInDiningModifier = 0;

        this.playerNumber = playerNumber;
        this.expertMode = expertMode;

        studentsForClouds = playerNumber == 3 ? 4 : 3;
        studentsForBoards = playerNumber == 3 ? 9 : 7;
        studentsToMove = playerNumber == 3 ? 4 : 3;
        movedStudentsInTurn = 0;
        movedFromCloudInTurn = 0;


        //Islands setup
        islands = new ArrayList<>();
        for(int i = 0; i < islandsNumber; i++) {
            this.islands.add(new Island(this));
        }
        //Place mother nature on the first island
        this.islands.get(0).setMotherNature(true);

        //Clouds and Players setup
        clouds = new ArrayList<>();
        players = new ArrayList<>();
        currentTurnOrder = new ArrayList<>();
        nextTurnOrder = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++) {
            this.clouds.add(new Cloud());
            Player newPlayer = new Player(i);
            this.players.add(newPlayer);
            this.currentTurnOrder.add(newPlayer);
        }
        turnNumber = 0;

        //Teams
        teams = new ArrayList<>();
        switch (playerNumber) {
            case 2 -> {
                //2 players: 2 teams
                teams.add(new Team(TowerColor.BLACK, 8, 0));
                teams.add(new Team(TowerColor.WHITE, 8, 1));
                teams.get(0).addPlayer(players.get(0));
                teams.get(1).addPlayer(players.get(1));
            }
            case 3 -> {
                //3 players: 3 teams
                teams.add(new Team(TowerColor.BLACK, 6, 0));
                teams.add(new Team(TowerColor.WHITE, 6, 1));
                teams.add(new Team(TowerColor.GREY, 6, 2));
                teams.get(0).addPlayer(players.get(0));
                teams.get(1).addPlayer(players.get(1));
                teams.get(2).addPlayer(players.get(2));
            }
            case 4 -> {
                //4 players: 2 teams
                teams.add(new Team(TowerColor.BLACK, 8, 0));
                teams.add(new Team(TowerColor.WHITE, 8, 1));
                teams.get(0).addPlayer(players.get(0));
                teams.get(0).addPlayer(players.get(1));
                teams.get(1).addPlayer(players.get(2));
                teams.get(1).addPlayer(players.get(3));
            }
        }

        //Set player's team id in their class
        updateTeams();

        //Bag setup
        this.students = new ArrayList<>();
        for(int i = 0; i < studentNumber; i++) {
            this.students.add(i);
        }

        // ------------------- //
        // Characters creation //
        // ------------------- //

        if(expertMode) {

            this.allCharacters = new ArrayList<>(); //List of all existing characters
            for (int i = 1; i <= allCharactersNumber; i++) {
                allCharacters.add(i);
            }
            Collections.shuffle(allCharacters); //Random shuffle of all existing characters

            String[] characterJsonName = new String[availableCharactersNumber]; //Array of characters JSON paths
            //Pick n random characters from all the existing ones
            for (int i = 0; i < availableCharactersNumber; i++) {
                // Create the paths
                characterJsonName[i] = "Character" + allCharacters.get(i) + ".JSON";
            }

            this.availableCharacters = new Character[availableCharactersNumber]; //Array of n characters
            for (int i = 0; i < availableCharactersNumber; i++) {
                /* OPEN JSON */
                try {
                    Gson gson = new Gson();
                    Reader reader = new InputStreamReader(Eriantys.class.getResourceAsStream("/characters/" + characterJsonName[i]));
                    availableCharacters[i] = gson.fromJson(reader, Character.class);
                    availableCharacters[i].students = new HashSet<>();
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            allCharacters = null;
            availableCharacters = null;
        }

        this.players.get(0).setNickname(nicknameOfCreator);
        this.players.get(0).setActive(true);
        this.currentPlayer = players.get(0).getNickname();
        this.setupFill();
    }

    /**
     * Fills all the pieces that need a setup at the start of the game
     */
    public void setupFill(){

        //Get 10 students, 2 of each color
        List<Integer> studentsForIslands = new ArrayList<>();
        for(int i = 0; i < 2; i++){
            studentsForIslands.add(getAStudent(Color.YELLOW));
            studentsForIslands.add(getAStudent(Color.BLUE));
            studentsForIslands.add(getAStudent(Color.GREEN));
            studentsForIslands.add(getAStudent(Color.RED));
            studentsForIslands.add(getAStudent(Color.PURPLE));
        }
        Collections.shuffle(studentsForIslands);

        //Fill islands (except first one and last one)
        Integer student;
        int count = 1;
        for (Island island : this.islands.subList(1, islands.size())) {
            if(count==(islands.size()/2)){
                count++;
                continue;
            }
            student = studentsForIslands.get(0);
            studentsForIslands.remove(0);
            island.addStudent(student);
            count++;
        }

        //Fill clouds
        for (Cloud cloud : this.clouds) {
            for(int i = 0; i < studentsForClouds; i++){
                cloud.addStudent(getAStudent());
            }
        }

        //Fill schoolboards
        for (Player player : this.getPlayers()) {
            //Add 7/9 random students
            for(int i = 0; i < studentsForBoards; i++){
                player.getPlayerBoard().addStudent(getAStudent());
            }
        }

        //Fill characters
        if(expertMode) {
            for (Character character : this.availableCharacters) {
                switch (character.getSetupObject()) {
                    case "student":
                        //Adds n students from the bag
                        for (int i = 0; i < character.getSetupNumber(); i++) {
                            character.addStudent(this.getAStudent());
                        }
                        break;
                    case "no_entry":
                        //Adds n noEntry tiles
                        character.setNoEntryNumber(character.getSetupNumber());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * Sets team ID in player objects
     */
    public void updateTeams(){
        teams.forEach(team ->team.getPlayers()
                .forEach(player -> player.setTeamID(team.getTeamId())));
    }

    /**
     * Changes current player, resets modifiers. If planning phase ends, moves to action. If action phase ends,
     * skips to next turn. Updates turn orders accordingly.
     * If there is only 1 player connected, start a 10 sec timer, if nobody reconnects, sends the game (the winner is
     * the last online player).
     */
    public void nextPlayer() {

        //Only progress if there isn't any active timer. If there is, just wait
        if(!startedTimer) {
            if (students.isEmpty() || anyWinner()) {
                endGame();
            }

            this.resetModifiers();
            int currentIndex = currentTurnOrder.indexOf(this.getPlayerByNickname(currentPlayer));
            //If current is the last element
            if (currentIndex == currentTurnOrder.size() - 1) {

                //If all players played an assistant, change to action phase
                if (currentPhase.equals(GamePhase.PLANNING)) {

                    //Update order and move to action order
                    updateNextTurnOrder();
                    this.setCurrentTurnOrder(actionPhaseOrder);
                    this.setCurrentPlayer(actionPhaseOrder.get(0).getNickname());
                    this.setCurrentPhase(GamePhase.ACTION);

                } else {
                    //If the game bag is empty, the game ends
                    if (students.size() <= 0) {
                        endGame();
                    }
                    //Action phase ended, next turn starting
                    this.setCurrentTurnOrder(nextTurnOrder);
                    this.setCurrentPlayer(currentTurnOrder.get(0).getNickname());
                    this.setCurrentPhase(GamePhase.PLANNING);
                    this.turnNumber++;
                    //Reset last assistant played
                    this.players.forEach(player -> player.setLastAssistantPlayed(null));
                    //Refill clouds for new planning phase
                    this.turnStartFill();
                }

            } else {
                //Next player
                this.setCurrentPlayer(currentTurnOrder.get(currentIndex + 1).getNickname());
            }
            //If there is only 1 player connected, start timer (only if the game has not already ended)
            if (connectedPlayersNumber() <= 1 && !currentPhase.equals(END)) {
                System.out.println("Started 10sec timer because only 1 or less players are still connected");
                startedTimer = true;
                ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
                timer.schedule(() -> {
                    if (connectedPlayersNumber() <= 1) {
                        System.out.println("Timer ended, the players did not reconnect. Ending the game");
                        Player winner = players.stream()
                                .filter(Player::isActive)
                                .findFirst()
                                .orElse(players.get(0));
                        winnerTeam = this.getTeamByID(winner.getTeamID());
                        endGame();
                    } else {
                        startedTimer = false;
                    }
                }, gameEndTimout, TimeUnit.SECONDS);
            } else {
                //If the player is disconnected, skip his turn
                Player newNextPlayer = getPlayerByNickname(currentPlayer);
                if (!newNextPlayer.isActive()) {
                    if (currentPhase.equals(PLANNING)) {
                        newNextPlayer.setLastAssistantPlayed(new Assistant(11, 0, -1));
                    }
                    nextPlayer();
                }
            }
        } else {
            nextPlayer();
        }
    }

    /**
     * @return the number of active (connected) players
     */
    public Integer connectedPlayersNumber(){
        Integer counter = 0;
        for(Player player : players){
            if(player.isActive()){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Updates the order of the next turn, depending on the assistants played
     */
    public void updateNextTurnOrder(){

        this.actionPhaseOrder = players.stream()
                .sorted(Comparator.comparingInt(Player::getLastAssistantPlayedPriority))
                .collect(Collectors.toList());

        Integer firstPlayerIndex = actionPhaseOrder.get(0).getPlayerId();
        List<Player> newNextTurnOrder = new ArrayList<>();

        for(int i = 0; i < players.size(); i++){
            //first player = who won the character phase. Next players selected clockwise (following players original list)
            newNextTurnOrder.add(players.get( (firstPlayerIndex + i) % players.size() ));
        }

        this.setNextTurnOrder(newNextTurnOrder);
    }

    /**
     * Same as SetupFill(), but done every start of turn to refill empty clouds
     */
    public void turnStartFill(){

        //Fill clouds
        for (Cloud cloud : this.clouds) {
            for(int i = cloud.getStudents().size(); i < studentsForClouds; i++){
                if(students.isEmpty()){
                    return;
                }
                cloud.addStudent(getAStudent());
            }
        }
    }

    /**
     * Gets (and removes) a random student from the game bag
     *
     * @return a random student from the bag
     */
    public Integer getAStudent() {
        int studentSize = this.students.size();
        int randomStudent = new Random().nextInt(studentSize);
        int studentToGet = this.students.get(randomStudent);
        this.students.remove(randomStudent);
        this.students.trimToSize();
        return studentToGet;
    }

    /**
     * Gets (and removes) a random student (of input color) from the game bag.
     * Used only in the game setup, for the islands.
     *
     * @param color color of the student to get
     * @return a random student (of input color) from the bag
     */
    public Integer getAStudent(Color color) { //get (and remove) a certain color student from the game bag
        int studentSize = this.students.size();
        int randomStudent = new Random().nextInt(studentSize);
        int studentToGet = this.students.get(randomStudent);
        while(!colorOfStudent(studentToGet).equals(color)){
            randomStudent = new Random().nextInt(studentSize);
            studentToGet = this.students.get(randomStudent);
        }
        this.students.remove(randomStudent);
        this.students.trimToSize();
        return studentToGet;
    }

    /**
     * Handles resolving the island and unifying it with adjacent ones accordingly to the rules.
     * Loops until all unifiable islands are merged. For unifying, see Game.unifyIsland(Island, Island)
     *
     * @param island island to resolve
     */
    public void resolveIsland(Island island){

        boolean unified;
        int refIndex;
        island.resolve(this.teams);
        if(anyWinner()){
            endGame();
            return;
        }

        do {
            //Check if there are 3 or less remaining islands. If true, game ends
            if(islands.size() <= 3){
                endGame();
                return;
            }

            refIndex = islands.indexOf(island);
            Island nextIsland = islands.get((refIndex + 1) % (islands.size()));
            Island previousIsland;
            if(refIndex == 0){
                previousIsland = islands.get(islands.size() - 1);
            }else{
                previousIsland = islands.get((refIndex - 1) % (islands.size()));
            }

            if (island.getTowersColor() != null
                    && island.getTowersColor() == nextIsland.getTowersColor()) {
                this.unifyIslands(island, nextIsland);
                unified = true;
            } else if (island.getTowersColor() != null
                    && island.getTowersColor() == previousIsland.getTowersColor()) {
                this.unifyIslands(island, previousIsland);
                unified = true;
            } else {
                unified = false;
            }

        } while(unified);
    }

    /**
     * Merges 2 islands. The first one is kept, the second one is deleted after adding all its content to the first one
     *
     * @param toKeep island to be kept
     * @param toRemove island to be deleted
     */
    public void unifyIslands(Island toKeep, Island toRemove){

        toKeep.getStudents().addAll(toRemove.getStudents());
        toKeep.setTowersNumber(toKeep.getTowersNumber() + toRemove.getTowersNumber());
        toKeep.setNoEntry(toKeep.getNoEntry() + toRemove.getNoEntry());
        if(toRemove.isMotherNature()){
            toKeep.setMotherNature(true);
        }
        islands.remove(toRemove);
    }

    /**
     * @param nickname of the player to add
     * @return false if the game is full, true otherwise
     */
    public Boolean addPlayer(String nickname){

        if(emptyPlayerNumber >= playerNumber){
            return false;
        } else {
            this.players.get(emptyPlayerNumber).setNickname(nickname);
            this.players.get(emptyPlayerNumber).setActive(true);
            emptyPlayerNumber++;
            if(emptyPlayerNumber.equals(playerNumber)) {
                this.currentPhase = PLANNING;
            }
            return true;
        }
    }

    /**
     * Resets turn modifiers
     */
    public void resetModifiers(){

        //Reset moved students
        movedStudentsInTurn = 0;
        movedFromCloudInTurn = 0;
        movedMotherNatureInTurn = false;

        this.setAllStudentsValue(1);
        this.setTowerValue(1);
        this.setInfluenceModifier(0);
        this.setMotherNatureMovements(0);
        this.setStudentsInDiningModifier(0);

        for (Player player : this.getPlayers()) {
            player.setActiveCharacter(false);
            //player.setLastAssistantPlayed(null);
        }

        if(this.isExpertMode()) {
            for (Character character : this.getAllCharacters()) {
                character.setHasBeenUsed(false);
            }
        }
    }

    /**
     * if there's not yet a winner, finds one and ends game
     */
    public void endGame(){

        //If there is not yet a winner
        if(!anyWinner()){
            processTheWinner();
        }
        currentPhase = END;
        System.out.println("----------------------------");
        System.out.println("THE GAME HAS ENDED. WINNER: Team " + winnerTeam.getTeamId());
        System.out.print("Member(s): ");
        for (Player player : winnerTeam.getPlayers()){
            System.out.print(player.getNickname() + " ");
        }
        System.out.println();
        System.out.println("----------------------------");
    }

    /**
     * @return true if there's a winner, false otherwise
     */
    public Boolean anyWinner(){

        if(winnerTeam != null){
            return true;
        }

        for(Team team : this.teams){
            if(team.isWinner()) {
                winnerTeam = team;
                return true;
            }
        }

        return false;
    }

    /**
     * finds the winner team if any
     */
    public void processTheWinner(){

        //Way higher than max available towers
        Integer minTowers = 10;
        Team tempWinner = new Team();
        for (Team team : teams) {
            if(team.getTowerNumber() < minTowers){
                minTowers = team.getTowerNumber();
                tempWinner = team;
            } else if (team.getTowerNumber().equals(minTowers)) {
                //If there is a draw, check who has more professors
                tempWinner = team.getProfessorsNumber() > tempWinner.getProfessorsNumber()
                        ? team
                        : tempWinner;
            }
        }
        winnerTeam = tempWinner;
    }

    //Getters

    /**
     * @return island on which there is mother nature
     */
    public Island getMotherNatureIsland(){

        for(Island island : islands){
            if(island.isMotherNature()){
                return island;
            }
        }
        return null;
    }

    /**
     * @return list of the players
     */
    public List<Player> getPlayers() {

        List<Player> playerList = new ArrayList<>();
        for (Team team: teams) {
            playerList.addAll(team.getPlayers());
        }
        return playerList;

    }

    /**
     * @return current game phase
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * @return teams
     */
    public ArrayList<Team> getTeams() {
        return teams;
    }

    /**
     * @return bag students
     */
    public ArrayList<Integer> getBagStudents(){
        return students;
    }

    /**
     * @return islands
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * @return clouds
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * @return nextTurnOrder
     */
    public List<Player> getNextTurnOrder() {
        return nextTurnOrder;
    }

    /**
     * @return currentTurnOrder
     */
    public List<Player> getCurrentTurnOrder() {
        return currentTurnOrder;
    }

    /**
     * @param index index of character to get
     * @return the requested character
     */
    public Character getCharacter(int index) {
        return availableCharacters[index];
    }

    /**
     * @return all available Characters
     */
    public Character[] getAllCharacters(){
        return this.availableCharacters;
    }

    /**
     * @return currentPlayer
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return expertMode
     */
    public Boolean isExpertMode() {
        return expertMode;
    }

    /**
     * @return turnNumber
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * @return a reduced model for the view
     */
    public Game getReducedModel() {
        return new Game(this);
    }

    /**
     * @return winnerTeam
     */
    public Team getWinnerTeam() {
        return winnerTeam;
    }

    /**
     * @return students that can be moved in the round
     */
    public int getStudentsToMove() {
        return studentsToMove;
    }

    /**
     * @return number of students moved from a cloud in this turn
     */
    public int getMovedFromCloudInTurn() {
        return movedFromCloudInTurn;
    }

    /**
     * @return number of students for each clouds
     */
    public int getStudentsForClouds() {
        return studentsForClouds;
    }

    /**
     * @return if mother nature has already been selected in the round
     */
    public boolean getMovedMotherNatureInTurn() {
        return movedMotherNatureInTurn;
    }

    //Setters

    /**
     * @param movedMotherNatureInTurn set if mother nature has already been selected in the round
     */
    public void setMovedMotherNatureInTurn(Boolean movedMotherNatureInTurn){
        this.movedMotherNatureInTurn = movedMotherNatureInTurn;
    }

    /**
     * @param currentTurnOrder the current turn order
     */
    public void setCurrentTurnOrder(List<Player> currentTurnOrder) {
        this.currentTurnOrder = currentTurnOrder;
    }

    /**
     * @param currentPhase the current phase
     */
    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * @param nextTurnOrder the next turn order
     */
    public void setNextTurnOrder(List<Player> nextTurnOrder) {
        this.nextTurnOrder = nextTurnOrder;
    }

    /**
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    //Getters

    /**
     * @param colorID ID of the color
     * @return value of the student color
     */
    public Integer getStudentValue(int colorID){
        return studentValue[colorID];
    }

    /**
     * @return towerValue
     */
    public Integer getTowerValue(){
        return towerValue;
    }

    /**
     * @return influenceModifier
     */
    public Integer getInfluenceModifier() {
        return influenceModifier;
    }

    /**
     * @return motherNatureMovements
     */
    public Integer getMotherNatureMovementsModifier() {
        return motherNatureMovements;
    }

    /**
     * @return studentsInDiningModifier
     */
    public Integer getStudentsInDiningModifier() {
        return studentsInDiningModifier;
    }

    /**
     * @return a unique id of StudentAccessiblePieces
     */
    public static Integer getNextPieceID(){
        nextPieceID++;
        return nextPieceID-1;
    }

    /**
     * @return students moved in this turn
     */
    public int getMovedStudentsInTurn() {
        return movedStudentsInTurn;
    }

    //Setters

    /**
     * @param movedStudentsInTurn students moved in this turn
     */
    public void setMovedStudentsInTurn(int movedStudentsInTurn) {
        this.movedStudentsInTurn = movedStudentsInTurn;
    }

    /**
     * @param movedFromCloudInTurn students moved from a cloud in this turn
     */
    public void setMovedFromCloudInTurn(int movedFromCloudInTurn) {
        this.movedFromCloudInTurn = movedFromCloudInTurn;
    }

    /**
     * @param towerValue tower value to be set
     */
    public void setTowerValue(Integer towerValue) {
        this.towerValue = towerValue;
    }

    /**
     * @param color color of the student
     * @param studentValue value of the student
     */
    public void setStudentValue(Color color, Integer studentValue) {
        this.studentValue[StudentAccessiblePiece.indexOfColor(color)] = studentValue;
    }

    /**
     * @param studentValue value of all students
     */
    public void setAllStudentsValue(Integer studentValue) {
        for (int i = 0; i < 5; i++){
            this.studentValue[i] = studentValue;
        }
    }

    /**
     * @param newInfluenceModifier influence modifier
     */
    public void setInfluenceModifier(Integer newInfluenceModifier) {
        this.influenceModifier = newInfluenceModifier;
    }

    /**
     * @param newMotherNatureMovements Mother nature movements modifier
     */
    public void setMotherNatureMovements(Integer newMotherNatureMovements) {
        this.motherNatureMovements = newMotherNatureMovements;
    }

    /**
     * @param studentsInDiningModifier Students in dining number modifier
     */
    public void setStudentsInDiningModifier(Integer studentsInDiningModifier) {
        this.studentsInDiningModifier = studentsInDiningModifier;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // GET BY ID SECTION, USED BY THE CONTROLLER -----------------------------------------------------------------------

    /**
     * @param playerId ID of player to get
     * @return Player reference corresponding to te id
     */
    public Player getPlayerById(Integer playerId) {

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getPlayerId().equals(playerId))
                    return player;
            }
        }
        return null;

    }

    /**
     * @param playerNickname nickname of player to get
     * @return Player reference corresponding to the nickname, null if not found
     */
    public Player getPlayerByNickname(String playerNickname) {

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getNickname().equals(playerNickname))
                    return player;
            }
        }
        return null;
    }

    /**
     * @param boardID ID of the school board needed
     * @return SchoolBoard reference corresponding to the ID, null if not found
     */
    public SchoolBoard getSchoolBoardByID(Integer boardID) {
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if(player.getPlayerBoard().getPieceID().equals(boardID))
                    return player.getPlayerBoard();
            }
        }
        return null;
    }

    /**
     * @param islandID ID of the island needed
     * @return Island reference corresponding to the ID, null if not found
     */
    public Island getIslandByID(Integer islandID) {
        for (Island island : islands) {
            if(Objects.equals(island.getPieceID(), islandID))
                return island;
        }
        return null;
    }

    /**
     * @param cloudID ID of the cloud needed
     * @return cloud reference corresponding to the ID, null if not found
     */
    public Cloud getCloudByID(Integer cloudID) {
        for (Cloud cloud : clouds) {
            if(Objects.equals(cloud.getPieceID(), cloudID))
                return cloud;
        }
        return null;
    }

    /**
     * @param pieceID ID of the piece needed
     * @return StudentAccessiblePiece reference corresponding to the ID, null if not found
     */
    public StudentAccessiblePiece getStudentAccessiblePieceByID(Integer pieceID) {
        //SchoolBoards
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if(Objects.equals(player.getPlayerBoard().getPieceID(), pieceID)) {
                    return player.getPlayerBoard();
                }
            }
        }
        //Characters
        if(expertMode) {
            for (Character character : availableCharacters) {
                if (Objects.equals(character.getPieceID(), pieceID)) {
                    return character;
                }
            }
        }
        //Clouds
        for (Cloud cloud : clouds) {
            if(Objects.equals(cloud.getPieceID(), pieceID)){
                return cloud;
            }
        }
        //Islands
        for (Island island : islands) {
            if(Objects.equals(island.getPieceID(), pieceID)){
                return island;
            }
        }

        //No piece found
        return null;
    }

    /**
     * @param teamID ID of the team needed
     * @return Team reference corresponding to the ID, null if not found
     */
    public Team getTeamByID(Integer teamID){
        return teams.stream()
                .filter(team -> team.getTeamId()
                .equals(teamID))
                .findAny()
                .orElse(null);
    }

    /**
     * @param availableCharacters new availableCharacters array
     */
    public void setAvailableCharacters(Character[] availableCharacters) {
        this.availableCharacters = availableCharacters;
    }

    /**
     * @param gameEndTimout new timeout to replace default for player disconnection
     */
    public void setGameEndTimout(Integer gameEndTimout) {
        this.gameEndTimout = gameEndTimout;
    }
}
