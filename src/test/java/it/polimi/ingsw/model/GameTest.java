package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static it.polimi.ingsw.model.Game.getStudentValue;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    @DisplayName("Testing student movement from the game bag and the appropriate return index of colors")
    void testGetAStudent() {

        Game testGame = new Game(2, "test",true);
        ArrayList<Integer> testStudents = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            testStudents.add(testGame.getAStudent());
        }

        assertEquals(10, testStudents.size());
        int remainingStudents = 120 - 10 - 7*2 - 3*2;
        for (Character character : testGame.getAllCharacters()) {
            remainingStudents -= character.getStudents().size();
        }
        assertEquals(remainingStudents, testGame.getBagStudents().size());

        assertFalse(testGame.getBagStudents().contains(testStudents));

        assertEquals(0, indexOfColor(Color.YELLOW), "Index 0 should correspond to yellow");
        assertEquals(1, indexOfColor(Color.BLUE), "Index 1 should correspond to blue");
        assertEquals(2, indexOfColor(Color.GREEN), "Index 2 should correspond to red");
        assertEquals(3, indexOfColor(Color.RED), "Index 3 should correspond to green");
        assertEquals(4, indexOfColor(Color.PURPLE), "Index 4 should correspond to purple");
    }

    @Test
    @DisplayName("Testing player recovery by ID")
    void testGetPlayerByID(){

        Game testGame = new Game(2, "test", true);

        assertNull(testGame.getPlayerById(3));
        assertTrue(testGame.getPlayers().contains(testGame.getPlayerById(1)));
        assertTrue(testGame.getPlayers().contains(testGame.getPlayerById(0)));

    }

    @Test
    @DisplayName("Testing player recovery by ID")
    void testGetPlayers(){

        Game testGame2 = new Game(2, "test2", true);
        assertEquals(testGame2.getPlayers().size(), 2);

        Game testGame3 = new Game(3, "test3", true);
        assertEquals(testGame3.getPlayers().size(), 3);

        Game testGame4 = new Game(4, "test4", true);
        assertEquals(testGame4.getPlayers().size(), 4);

    }

    @Test
    @DisplayName("Testing character creation")
    void testCharacterGeneration() {

        String characterJsonName = ".\\src\\Characters\\" + "Character" + "7" + ".JSON";
        System.out.println(characterJsonName);

        Character testCharacter;

        /* OPEN JSON */
        try {
            // create Gson instance
            Gson gson = new Gson();
            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(characterJsonName));
            // convert JSON string to Character object
            testCharacter = gson.fromJson(reader, Character.class);
            // close reader
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            testCharacter = null;
        }

        assertNotNull(testCharacter);

    }

    @Test
    @DisplayName("Testing player managing")
    void testPlayerManaging() {
        Game gameTest = new Game(2, "Test", true);
        assertTrue(gameTest.addPlayer("Gianfranco"));
        assertFalse(gameTest.addPlayer("BRone"));
        gameTest.setCurrentPlayer("Gianfranco");
        assertEquals(gameTest.getCurrentPlayer(),"Gianfranco");
    }

    @Test
    @DisplayName("Testing character effects")
    void testCharacterEffects() {
        Game gameTest = new Game(2, "Test", true);
        assertNotNull(gameTest.getAllCharacters());
        gameTest.setStudentValue(Color.BLUE,3);
        assertEquals(3,Game.getStudentValue(indexOfColor(Color.BLUE)));
        gameTest.setAllStudentsValue(4);
        for(int i=0;i<5;i++){
            assertEquals(4,Game.getStudentValue(i));
        }
        gameTest.setStudentsInDiningModifier(2);
        assertEquals(gameTest.getStudentsInDiningModifier(),2);
        assertNotNull(gameTest.getPlayerByNickname("Test"));
    }

    @Test
    @DisplayName("Testing miscellanea")
    void testMiscellanea(){

        Game gameTest = new Game(2, "TestStudentValue", true);

        //public static Integer getStudentValue(int studentID)

        Integer studentID1 = 0;
        Integer studentID2 = 26;
        Integer studentID3 = 52;
        Integer studentID4 = 78;
        Integer studentID5 = 104;

        assertEquals(getStudentValue(indexOfColor(colorOfStudent(studentID1))), 1);
        assertEquals(getStudentValue(indexOfColor(colorOfStudent(studentID2))), 1);
        assertEquals(getStudentValue(indexOfColor(colorOfStudent(studentID3))), 1);
        assertEquals(getStudentValue(indexOfColor(colorOfStudent(studentID4))), 1);
        assertEquals(getStudentValue(indexOfColor(colorOfStudent(studentID5))), 1);

        //Getter and setters
        assertEquals(Game.getTowerValue(), 1);
        Game.setTowerValue(0);
        assertEquals(Game.getTowerValue(), 0);

        assertNotNull(gameTest.getTeams());
        assertNotNull(gameTest.getClouds());

        assertNotNull(gameTest.getCharacter(0));
        assertNotNull(gameTest.getCharacter(1));
        assertNotNull(gameTest.getCharacter(2));

        assertEquals(Game.getInfluenceModifier(), 0);
        Game.setInfluenceModifier(1);
        assertEquals(Game.getInfluenceModifier(), 1);

        assertEquals(Game.getMotherNatureMovements(), 0);
        Game.setMotherNatureMovements(1);
        assertEquals(Game.getMotherNatureMovements(), 1);

        gameTest.setCurrentTurnOrder(new ArrayList<>());
        assertTrue(gameTest.getCurrentTurnOrder().isEmpty());
        gameTest.setNextTurnOrder(new ArrayList<>());
        assertTrue(gameTest.getNextTurnOrder().isEmpty());

        //getIslandByID
        gameTest = new Game(2, "testGetIslandByID", true);
        Island anIsland = gameTest.getIslands().get(0);
        Island tester = gameTest.getIslandByID(anIsland.getPieceID());
        assertEquals(tester.getPieceID(), anIsland.getPieceID());
        //Using wrong indexes
        tester = gameTest.getIslandByID(-99);
        assertNull(tester);
        SchoolBoard testerBoard = gameTest.getSchoolBoardByID(-99);
        assertNull(testerBoard);
        StudentAccessiblePiece testPiece = gameTest.getStudentAccessiblePieceByID(-99);
        assertNull(testPiece);

    }

    @Test
    @DisplayName("Testing turn rotation")
    void testTurns(){

        GameController gameController = new GameController();
        gameController.startGame(2, "turntest", true);
        gameController.addPlayer("player2");
        Game game = gameController.getCurrentGame();

        //Play planning phase
        gameController.playAssistant(game.getPlayerByNickname("turntest").getPlayerId(), 0);
        gameController.playAssistant("player2", 0);

        assertEquals(gameController.getCurrentGame().getCurrentPhase(), GamePhase.ACTION);

        //Go to next turn
        gameController.nextPlayer();
        gameController.nextPlayer();

        assertEquals(gameController.getCurrentGame().getCurrentPhase(), GamePhase.PLANNING);
    }

    @Test
    @DisplayName("Testing reduced model creation")
    void testReducedModel(){

        GameController gameController = new GameController();
        gameController.startGame(2, "model test", true);
        gameController.addPlayer("player2");

        Game originalGame = gameController.getCurrentGame();
        Game copyGame = originalGame.getReducedModel();

        assertEquals(originalGame.getCurrentPhase(), copyGame.getCurrentPhase());
        assertEquals(originalGame.getPlayers(), copyGame.getPlayers());
        assertEquals(originalGame.getTeams(), copyGame.getTeams());
        assertEquals(originalGame.getIslands(), copyGame.getIslands());
        assertEquals(originalGame.getClouds(), copyGame.getClouds());
        assertEquals(originalGame.isExpertMode(), copyGame.isExpertMode());
    }

}