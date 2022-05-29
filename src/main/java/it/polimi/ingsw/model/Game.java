package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.Reader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static it.polimi.ingsw.model.GamePhase.*;

public class Game implements Serializable {

    //Constants
    private final Integer availableCharactersNumber = 3;
    private final Integer allCharactersNumber = 12;
    private final Integer islandsNumber = 12;
    private final Integer studentNumber = 130;
    private final ArrayList<Integer> allCharacters; //All existing characters
    private final String charactersJSONPath = ".\\src\\Characters\\";

    private String currentPlayer;
    private Integer emptyPlayerNumber = 1;
    private final Integer playerNumber;
    private List<Island> islands;
    private final Set<Cloud> clouds;
    private ArrayList<Integer> students; //This is the game bag
    private final ArrayList<Team> teams;
    private List<Player> players;
    private List<Player> currentTurnOrder; //contains playerIDs for the current round
    private List<Player> nextTurnOrder; //contains player IDs for the next round
    /* MAKE SURE TO COPY nextTurnOrder to currentTurnOrder BEFORE BEGINNING THE NEXT ROUND */
    private GamePhase currentPhase = OFF; //Game phase

    private final Character[] availableCharacters;

    //Modifiers
    private static Integer[] studentValue; //defaults to 1
    private static Integer towerValue; //defaults to 1
    private static Integer influenceModifier; //defaults to 0
    private static Integer motherNatureMovements; //defaults to 0
    private static Integer studentsInDiningModifier;

    //Needed for Piece ID
    private static Integer nextPieceID = 0;

    //Custom constructor for small model for the view
    private Game(Game fullGame){

        availableCharacters = new Character[availableCharactersNumber];
        int i = 0;

        for (Character character : fullGame.getAllCharacters()) {

            Integer cost = character.getCost();
            String image = character.getImage();
            Boolean hasIncreasedCost = character.getHasIncreasedCost();
            HashSet<Integer> students = character.getStudents();
            Integer noEntryNumber = character.getNoEntryNumber();

            availableCharacters[i] = new Character(cost, image, hasIncreasedCost, students, noEntryNumber);
            i++;
        }

        this.currentPhase = fullGame.getCurrentPhase();
        this.players = fullGame.getPlayers();
        this.playerNumber = fullGame.getPlayers().size();
        this.teams = fullGame.getTeams();
        this.islands = fullGame.getIslands();
        this.clouds = fullGame.getClouds();
        this.allCharacters = null;
    }

    public Game(int playerNumber, String nicknameOfCreator) {

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

        //Islands setup
        islands = new ArrayList<>();
        for(int i = 0; i < islandsNumber; i++) {
            this.islands.add(new Island());
        }
        //Place mother nature randomly
        int randomIsland = new Random().nextInt(islandsNumber);
        this.islands.get(randomIsland).setMotherNature(true);

        //Clouds and Players setup
        clouds = new HashSet<>();
        players = new ArrayList<>();
        currentTurnOrder = new ArrayList<>();
        nextTurnOrder = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++) {
            this.clouds.add(new Cloud());
            Player newPlayer = new Player(i);
            this.players.add(newPlayer);
            this.currentTurnOrder.add(newPlayer);
        }

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

        // DEBUG TO REMOVE
        //teams.get(0).addPlayer(players.get(0));
        //teams.get(1).addPlayer(players.get(1));

        //Bag setup
        this.students = new ArrayList<>();
        for(int i = 0; i < studentNumber; i++) {
            this.students.add(i);
        }

        // ------------------- //
        // Characters creation //
        // ------------------- //

        this.allCharacters  = new ArrayList<>(); //List of all existing characters
        for (int i=1; i <= allCharactersNumber; i++) {
            allCharacters.add(i);
        }
        Collections.shuffle(allCharacters); //Random shuffle of all existing characters

        String[] characterJsonName = new String[availableCharactersNumber]; //Array of characters JSON paths
        //Pick n random characters from all the existing ones
        for(int i = 0; i < availableCharactersNumber; i++){
            // Create the paths
            characterJsonName[i] = charactersJSONPath + "Character" + allCharacters.get(i) + ".JSON";
        }

        this.availableCharacters = new Character[availableCharactersNumber]; //Array of n characters
        for(int i = 0; i < availableCharactersNumber; i++){
            /* OPEN JSON */
            try {
                // create Gson instance
                Gson gson = new Gson();

                // create a reader
                Reader reader = Files.newBufferedReader(Paths.get(characterJsonName[i]));
                // convert JSON string to Character object
                availableCharacters[i] = gson.fromJson(reader, Character.class);
                // close reader
                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        this.players.get(0).setNickname(nicknameOfCreator);
        this.currentPlayer = players.get(0).getNickname();
    }

    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Integer getAStudent() { //get (and remove) a student from the game bag
        int studentSize = this.students.size();
        int randomStudent = new Random().nextInt(studentSize);
        int studentToGet = this.students.get(randomStudent);
        this.students.remove(randomStudent);
        this.students.trimToSize();
        return studentToGet;
    }

    public static Integer getStudentValue(int colorID){
        return studentValue[colorID];
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

    public static Integer getTowerValue(){
        return towerValue;
    }

    public static void setTowerValue(Integer towerValue) {
        Game.towerValue = towerValue;
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

    public Set<Cloud> getClouds() {
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
            if(emptyPlayerNumber == playerNumber) {
                this.currentPhase = PLANNING;
            }
            return true;
        }
    }

    public List<Player> getPlayers() {

        List<Player> playerList = new ArrayList<>();
        for (Team team: teams) {
            playerList.addAll(team.getPlayers());
        }
        return playerList;

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

    // STATIC SECTION --------------------------------------------------------------------------------------------------

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

    // -----------------------------------------------------------------------------------------------------------------

    // GET BY ID SECTION, USED BY THE CONTROLLER -----------------------------------------------------------------------

    public Player getPlayerById(Integer playerId) {

        for (Team team : teams) {
            for (Player player : team.getPlayers()) {
                if (player.getPlayerId() == playerId)
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
                if(player.getPlayerBoard().getPieceID() == boardID)
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
                if(player.getPlayerBoard().getPieceID() == pieceID) {
                    System.out.println("Found playerBoard: " + player.getPlayerBoard().getPieceID() + " searching for pieceID:" + pieceID);
                    return player.getPlayerBoard();
                }
            }
        }
        //Characters
        for (Character character : availableCharacters) {
            if(character.getPieceID() == pieceID){
                System.out.println("Found character: " + character.getPieceID() + " searching for pieceID:" + pieceID);
                return character;
            }
        }
        //Clouds
        for (Cloud cloud : clouds) {
            if(cloud.getPieceID() == pieceID){
                System.out.println("Found cloud: " + cloud.getPieceID() + " searching for pieceID:" + pieceID);
                return cloud;
            }
        }
        //Islands
        for (Island island : islands) {
            if(island.getPieceID() == pieceID){
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
            if(island.getPieceID() == islandID)
                return island;
        }
        System.out.println("Island with id " + islandID + " not found (Game.getIslandByID)");
        return null;
    }

    public static Integer getNextPieceID(){
        nextPieceID++;
        return nextPieceID-1;
    }

    public Game getReducedModel(){
        return new Game(this);
    }

}
