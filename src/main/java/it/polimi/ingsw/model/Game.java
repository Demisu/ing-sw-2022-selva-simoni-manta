package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.GamePhase.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

public class Game implements Serializable {

    //Constants
    private final Integer availableCharactersNumber = 3;
    private final Integer allCharactersNumber = 12;
    private final Integer islandsNumber = 12;
    private final Integer studentNumber = 130;
    private final ArrayList<Integer> allCharacters; //All existing characters
    private final String charactersJSONPath = ".\\src\\Characters\\";

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

    private final Character[] availableCharacters;

    //Modifiers
    private static Integer[] studentValue; //defaults to 1
    private static Integer towerValue; //defaults to 1
    private static Integer influenceModifier; //defaults to 0
    private static Integer motherNatureMovements; //defaults to 0
    private static Integer studentsInDiningModifier; //defaults to 0

    //Needed for Piece ID
    private static Integer nextPieceID = 0;

    //Custom constructor for small model for the view
    private Game(Game fullGame){

        this.expertMode = fullGame.isExpertMode();
        this.availableCharacters = new Character[availableCharactersNumber];

        if(this.expertMode) {

            int i = 0;

            for (Character character : fullGame.getAllCharacters()) {

                Integer cost = character.getCost();
                String image = character.getImage();
                Boolean hasIncreasedCost = character.getHasIncreasedCost();
                HashSet<Integer> characterStudents = character.getStudents();
                Integer noEntryNumber = character.getNoEntryNumber();

                this.availableCharacters[i] = new Character(cost, image, hasIncreasedCost, characterStudents, noEntryNumber);
                i++;
            }
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
        this.students = fullGame.students;
        this.turnNumber = fullGame.turnNumber;
        this.winnerTeam = fullGame.winnerTeam;
    }

    public Game(int playerNumber, String nicknameOfCreator, Boolean expertMode) {

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

        //Islands setup
        islands = new ArrayList<>();
        for(int i = 0; i < islandsNumber; i++) {
            this.islands.add(new Island());
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
                characterJsonName[i] = charactersJSONPath + "Character" + allCharacters.get(i) + ".JSON";
            }

            this.availableCharacters = new Character[availableCharactersNumber]; //Array of n characters
            for (int i = 0; i < availableCharactersNumber; i++) {
                /* OPEN JSON */
                try {
                    // create Gson instance
                    Gson gson = new Gson();

                    // create a reader
                    Reader reader = Files.newBufferedReader(Paths.get(characterJsonName[i]));
                    // convert JSON string to Character object
                    availableCharacters[i] = gson.fromJson(reader, Character.class);
                    availableCharacters[i].students = new HashSet<>();
                    // close reader
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
        this.currentPlayer = players.get(0).getNickname();
        this.setupFill();
    }

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

    public void updateTeams(){
        teams.forEach(team ->team.getPlayers()
                .forEach(player -> player.setTeamID(team.getTeamId())));
    }

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
            //If there is only 1 player connected, start timer
            if (connectedPlayersNumber().equals(1) && !startedTimer) {
                System.out.println("Started 10sec timer because only 1 or less players are still connected");
                startedTimer = true;
                ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
                timer.schedule(() -> {
                    if (connectedPlayersNumber().equals(1)) {
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
                }, 10, TimeUnit.SECONDS);
            }
            //If the player is disconnected, skip his turn
            Player newNextPlayer = getPlayerByNickname(currentPlayer);
            if (!newNextPlayer.isActive()) {
                newNextPlayer.setLastAssistantPlayed(new Assistant(11, 0, -1));
                nextPlayer();
            }
        } else {
            nextPlayer();
        }
    }

    public Integer connectedPlayersNumber(){
        Integer counter = 0;
        for(Player player : players){
            if(player.isActive()){
                counter++;
            }
        }
        return counter;
    }

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

    public Integer getAStudent() { //get (and remove) a student from the game bag
        int studentSize = this.students.size();
        int randomStudent = new Random().nextInt(studentSize);
        int studentToGet = this.students.get(randomStudent);
        this.students.remove(randomStudent);
        this.students.trimToSize();
        return studentToGet;
    }

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

    public void resolveIsland(Island island){

        boolean unified;
        int refIndex;
        island.resolve(this.teams);
        if(anyWinner()){
            endGame();
        }

        do {
            //Check if there are 3 or less remaining islands. If true, game ends
            if(islands.size() <= 3){
                endGame();
            }

            refIndex = islands.indexOf(island);
            Island nextIsland = islands.get((refIndex + 1) % (islands.size()));
            Island previousIsland;
            if(refIndex == 0){
                previousIsland = islands.get(islands.size() - 1);
            }else{
                previousIsland = islands.get((refIndex - 1) % (islands.size()));
            }

            if (island.getTowersColor() == nextIsland.getTowersColor()) {
                this.unifyIslands(island, nextIsland);
                unified = true;
            } else if (island.getTowersColor() == previousIsland.getTowersColor()) {
                this.unifyIslands(island, previousIsland);
                unified = true;
            } else {
                unified = false;
            }

        } while(unified);
    }

    public void unifyIslands(Island toKeep, Island toRemove){

        toKeep.getStudents().addAll(toRemove.getStudents());
        toKeep.setTowersNumber(toKeep.getTowersNumber() + toRemove.getTowersNumber());
        toKeep.setNoEntry(toKeep.getNoEntry() + toRemove.getNoEntry());
        if(toRemove.isMotherNature()){
            toKeep.setMotherNature(true);
        }
        islands.remove(toRemove);
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public ArrayList<Integer> getBagStudents(){
        return students;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public List<Cloud> getClouds() {
        return clouds;
    }

    public Island getMotherNatureIsland(){

        Island requestedIsland = new Island();
        for(Island island : islands){
            if(island.isMotherNature()){
                requestedIsland = island;
            }
        }
        return requestedIsland;
    }

    public List<Player> getCurrentTurnOrder() {
        return currentTurnOrder;
    }

    public void setCurrentTurnOrder(List<Player> currentTurnOrder) {
        this.currentTurnOrder = currentTurnOrder;
    }

    public List<Player> getNextTurnOrder() {
        return nextTurnOrder;
    }

    public void setNextTurnOrder(List<Player> nextTurnOrder) {
        this.nextTurnOrder = nextTurnOrder;
    }

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

    public void resetModifiers(){

        Game.setAllStudentsValue(1);
        Game.setTowerValue(1);
        Game.setInfluenceModifier(0);
        Game.setMotherNatureMovements(0);
        Game.setStudentsInDiningModifier(0);

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

    public List<Player> getPlayers() {

        List<Player> playerList = new ArrayList<>();
        for (Team team: teams) {
            playerList.addAll(team.getPlayers());
        }
        return playerList;

    }

    public void endGame(){

        //If there is not yet a winner
        if(!anyWinner()){
            processTheWinner();
        }
        currentPhase = END;
    }

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

    public Character getCharacter(int index) {
        return availableCharacters[index];
    }

    public Character[] getAllCharacters(){
        return this.availableCharacters;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Boolean isExpertMode() {
        return expertMode;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public Game getReducedModel(){
        return new Game(this);
    }

    public Team getWinnerTeam() {
        return winnerTeam;
    }

    // STATIC SECTION --------------------------------------------------------------------------------------------------

    public static Integer getStudentValue(int colorID){
        return studentValue[colorID];
    }

    public static Integer getTowerValue(){
        return towerValue;
    }

    public static void setTowerValue(Integer towerValue) {
        Game.towerValue = towerValue;
    }

    public static void setStudentValue(Color color, Integer studentValue) {
        Game.studentValue[StudentAccessiblePiece.indexOfColor(color)] = studentValue;
    }

    public static void setAllStudentsValue(Integer studentValue) {
        for (int i = 0; i < 5; i++){
            Game.studentValue[i] = studentValue;
        }
    }

    public static Integer getInfluenceModifier() {
        return influenceModifier;
    }

    public static void setInfluenceModifier(Integer newInfluenceModifier) {
        influenceModifier = newInfluenceModifier;
    }

    public static Integer getMotherNatureMovements() {
        return motherNatureMovements;
    }

    public static void setMotherNatureMovements(Integer newMotherNatureMovements) {
        motherNatureMovements = newMotherNatureMovements;
    }

    public static void setStudentsInDiningModifier(Integer studentsInDiningModifier) {
        Game.studentsInDiningModifier = studentsInDiningModifier;
    }

    public static Integer getStudentsInDiningModifier() {
        return studentsInDiningModifier;
    }

    public static Integer getNextPieceID(){
        nextPieceID++;
        return nextPieceID-1;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // GET BY ID SECTION, USED BY THE CONTROLLER -----------------------------------------------------------------------

    public Player getPlayerById(Integer playerId) {

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getPlayerId().equals(playerId))
                    return player;
            }
        }
        System.out.println("Player " + playerId + " not found (Game.getPlayerById)");
        return null;

    }

    public Player getPlayerByNickname(String playerNickname) {

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getNickname().equals(playerNickname))
                    return player;
            }
        }
        System.out.println("Player " + playerNickname + " not found (Game.getPlayerByNickname)");
        return null;
    }

    public SchoolBoard getSchoolBoardByID(Integer boardID) {
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if(player.getPlayerBoard().getPieceID().equals(boardID))
                    return player.getPlayerBoard();
            }
        }
        System.out.println("SchoolBoard with id " + boardID + " not found (Game.getSchoolBoardByID)");
        return null;
    }

    public StudentAccessiblePiece getStudentAccessiblePieceByID(Integer pieceID) {
        //SchoolBoards
        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if(Objects.equals(player.getPlayerBoard().getPieceID(), pieceID)) {
                    System.out.println("Found playerBoard: " + player.getPlayerBoard().getPieceID() + " searching for pieceID:" + pieceID);
                    return player.getPlayerBoard();
                }
            }
        }
        //Characters
        for (Character character : availableCharacters) {
            if(Objects.equals(character.getPieceID(), pieceID)){
                System.out.println("Found character: " + character.getPieceID() + " searching for pieceID:" + pieceID);
                return character;
            }
        }
        //Clouds
        for (Cloud cloud : clouds) {
            if(Objects.equals(cloud.getPieceID(), pieceID)){
                System.out.println("Found cloud: " + cloud.getPieceID() + " searching for pieceID:" + pieceID);
                return cloud;
            }
        }
        //Islands
        for (Island island : islands) {
            if(Objects.equals(island.getPieceID(), pieceID)){
                System.out.println("Found island: " + island.getPieceID() + " searching for pieceID:" + pieceID);
                return island;
            }
        }

        //No piece found
        System.out.println("StudentAccessiblePiece with id " + pieceID + " not found (Game.getStudentAccessiblePieceByID)");
        return null;
    }

    public Island getIslandByID(Integer islandID) {
        for (Island island : islands) {
            if(Objects.equals(island.getPieceID(), islandID))
                return island;
        }
        System.out.println("Island with id " + islandID + " not found (Game.getIslandByID)");
        return null;
    }

    public Team getTeamByID(Integer teamID){
        return teams.stream()
                .filter(team -> team.getTeamId()
                .equals(teamID))
                .findAny()
                .orElse(null);
    }
}
