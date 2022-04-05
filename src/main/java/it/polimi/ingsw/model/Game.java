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
    final String charactersJSONPath = "path/path/path...";

    private Integer currentPlayerID;
    private Integer playerNumber;
    private List<Island> islands;
    private Set<Cloud> clouds;
    private ArrayList<Integer> students; //This is the game bag
    private static ArrayList<ArrayList<Player>> teams;
    private List<Player> players;
    private List<Integer> turnOrder; //cantains playerID
    private Integer motherNatureMovements;
    private Character[] availableCharacters;

    private String[] characterJsonName;

    //Modifiers
    private static Integer[] studentValue; //defaults to 1
    private static Integer towerValue; //defaults to 1

    public Game(int playerNumber, String nicknameOfCreator) {

        //Piece values
        towerValue = 1;
        studentValue = new Integer[5];
        for(int i = 0; i < 5; i++){
            studentValue[i] = 1;
        }

        this.playerNumber = playerNumber;

        for(int i = 0; i < islandsNumber; i++) {
            this.islands.add(new Island(i));
        } 
        
        for(int i = 0; i < playerNumber; i++) {
            this.clouds.add(new Cloud(i));
            this.players.add(new Player(i));
        }

        this.students = new ArrayList<Integer>();
        for(int i = 0; i < studentNumber; i++) {
            this.students.add(i);
        }

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
            this.characterJsonName[i] = charactersJSONPath + "character" + allCharacters.get(i) + ".JSON";
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

    public static ArrayList<ArrayList<Player>> getTeams() {
        return teams;
    }
}
