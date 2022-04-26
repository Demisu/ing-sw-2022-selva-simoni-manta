package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Game {

    //Constants
    final Integer availableCharactersNumber = 3;
    final Integer allCharactersNumber = 12;
    final Integer islandsNumber = 12;
    final Integer studentNumber = 130;
    final ArrayList<Integer> allCharacters; //All existing characters
    final String charactersJSONPath = ".\\src\\Characters\\";

    private Integer currentPlayerID;
    private Integer playerNumber;
    private List<Island> islands;
    private Set<Cloud> clouds;
    private ArrayList<Integer> students; //This is the game bag
    private static ArrayList<Team> teams;
    private List<Player> players;
    private List<Player> currentTurnOrder; //contains playerIDs for the current round
    private List<Player> nextTurnOrder; //contains player IDs for the next round
    /* MAKE SURE TO COPY nextTurnOrder to currentTurnOrder BEFORE BEGINNING THE NEXT ROUND */

    private Character[] availableCharacters;

    private String[] characterJsonName;

    //Modifiers
    private static Integer[] studentValue; //defaults to 1
    private static Integer towerValue; //defaults to 1
    private static Integer influenceModifier; //defaults to 0
    private static Integer motherNatureMovements; //defaults to 0

    public Game(int playerNumber, String nicknameOfCreator) {

        //Piece values setup
        towerValue = 1;
        studentValue = new Integer[5];
        for(int i = 0; i < 5; i++){
            studentValue[i] = 1;
        }

        this.playerNumber = playerNumber;

        //Islands setup
        islands = new ArrayList<>();
        for(int i = 0; i < islandsNumber; i++) {
            this.islands.add(new Island(i));
        }
        //Place mother nature randomly
        int randomIsland = new Random().nextInt(islandsNumber);
        this.islands.get(randomIsland).setMotherNature(true);

        //Clouds and Players setup
        clouds = new HashSet<>();
        players = new ArrayList<>();
        for(int i = 0; i < playerNumber; i++) {
            this.clouds.add(new Cloud(i));
            this.players.add(new Player(i));
        }

        //Bag setup
        this.students = new ArrayList<Integer>();
        for(int i = 0; i < studentNumber; i++) {
            this.students.add(i);
        }

        //Influence setup
        influenceModifier = 0;

        // ------------------- //
        // Characters creation //
        // ------------------- //

        this.allCharacters  = new ArrayList<Integer>(); //List of all existing characters
        for (int i=1; i <= allCharactersNumber; i++) {
            allCharacters.add(i);
        }
        Collections.shuffle(allCharacters); //Random shuffle of all existing characters

        this.characterJsonName = new String[availableCharactersNumber]; //Array of characters JSON paths
        //Pick n random characters from all the existing ones
        for(int i = 0; i < availableCharactersNumber; i++){
            // Create the paths
            this.characterJsonName[i] = charactersJSONPath + "Character" + allCharacters.get(i) + ".JSON";
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
                availableCharacters[i] = gson.fromJson(reader,Character.class);
                // close reader
                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        this.players.get(0).setNickname(nicknameOfCreator);
    }

    public Player getPlayerById(Integer playerId) {

        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPlayerId() == playerId)
                return players.get(i);
        }
        return null;
    }

    public Integer getAStudent() { //get (and remove) a student from the game bag
        int studentSize = this.students.size();
        int randomStudent = new Random().nextInt(studentSize);
        int studentToGet = this.students.get(randomStudent);
        this.students.remove(randomStudent);
        this.students.trimToSize();
        return studentToGet;
    }

    public static Integer getStudentValue(int studentID){
        return switch (StudentAccessiblePiece.colorOfStudent(studentID)) {
            case YELLOW -> studentValue[0];
            case BLUE -> studentValue[1];
            case GREEN -> studentValue[2];
            case RED -> studentValue[3];
            case PURPLE -> studentValue[4];
        };
    }

    public static Integer getTowerValue(){
        return towerValue;
    }

    public static void setTowerValue(Integer towerValue) {
        Game.towerValue = towerValue;
    }

    public static ArrayList<Team> getTeams() {
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
        for(Island island : islands){
            if(island.isMotherNature()){
                return island;
            }
        }
        return new Island(9999);
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void unifyIslands(Island a, Island b){
        a.getStudents().addAll(b.getStudents());
        a.setTowersNumber(a.getTowersNumber() + b.getTowersNumber());
        a.setNoEntry(a.getNoEntry() + b.getNoEntry());
        if(b.isMotherNature()){
            a.setMotherNature(true);
        }
        this.islands.remove(b);
    }

    public Character getCharacter(int index) {
        return availableCharacters[index];
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
}
