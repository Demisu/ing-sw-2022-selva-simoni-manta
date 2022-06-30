package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

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
        assertEquals(3,gameTest.getStudentValue(indexOfColor(Color.BLUE)));
        gameTest.setAllStudentsValue(4);
        for(int i=0;i<5;i++){
            assertEquals(4,gameTest.getStudentValue(i));
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

        assertEquals(gameTest.getStudentValue(indexOfColor(colorOfStudent(studentID1))), 1);
        assertEquals(gameTest.getStudentValue(indexOfColor(colorOfStudent(studentID2))), 1);
        assertEquals(gameTest.getStudentValue(indexOfColor(colorOfStudent(studentID3))), 1);
        assertEquals(gameTest.getStudentValue(indexOfColor(colorOfStudent(studentID4))), 1);
        assertEquals(gameTest.getStudentValue(indexOfColor(colorOfStudent(studentID5))), 1);

        //Getter and setters
        assertEquals(gameTest.getTowerValue(), 1);
        gameTest.setTowerValue(0);
        assertEquals(gameTest.getTowerValue(), 0);

        assertNotNull(gameTest.getTeams());
        assertNotNull(gameTest.getClouds());

        assertNotNull(gameTest.getCharacter(0));
        assertNotNull(gameTest.getCharacter(1));
        assertNotNull(gameTest.getCharacter(2));

        assertEquals(gameTest.getInfluenceModifier(), 0);
        gameTest.setInfluenceModifier(1);
        assertEquals(gameTest.getInfluenceModifier(), 1);

        assertEquals(gameTest.getMotherNatureMovementsModifier(), 0);
        gameTest.setMotherNatureMovements(1);
        assertEquals(gameTest.getMotherNatureMovementsModifier(), 1);

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

        gameTest.getTeams().get(0).setWinner(true);
        assertTrue(gameTest.anyWinner());
        Team team = gameTest.getWinnerTeam();
        int i = gameTest.getStudentsToMove();
        i = gameTest.getTurnNumber();
        gameTest.setMovedFromCloudInTurn(gameTest.getMovedFromCloudInTurn());
        gameTest.getCloudByID(gameTest.getClouds().get(0).getPieceID());
        gameTest.setMovedStudentsInTurn(gameTest.getMovedStudentsInTurn());

        gameTest.getClouds().get(0).setStudents(new HashSet<>());
        gameTest.getTeams().forEach(t -> t.getPlayers().forEach(p -> p.setActive(false)));

        Game gameNoExp = new Game(2, "noExp", false);
        assertFalse(gameNoExp.isExpertMode());

        assertNull(gameNoExp.getCloudByID(-1));
        assertNull(gameNoExp.getPlayerByNickname("wrong"));
        assertFalse(gameNoExp.getMovedMotherNatureInTurn());
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
        game.nextPlayer();
        game.nextPlayer();

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

    @Test
    @DisplayName("Testing end of the game")
    void testEndGame(){

        Game game = new Game(2, "test", true);
        Team test = game.getTeamByID(0);
        game.setGameEndTimout(0);
        game.getPlayers().get(0).setActive(false);
        game.nextPlayer();
        game.endGame();
        for(int i = 0; i < Integer.MAX_VALUE; i++){
            i++;
            i--;
        }
        assertEquals(game.getCurrentPhase(), GamePhase.END);
    }

    @Test
    @DisplayName("Testing player disconnection")
    void testDisconnect(){

        GameController controller = new GameController();
        controller.startGame(2, "test", true);
        Game game = controller.getCurrentGame();
        controller.addPlayer("test2");
        controller.setInactive("test2");
        assertFalse(game.getPlayers().get(1).isActive());
        assertEquals(game.connectedPlayersNumber(), 1);
    }

    @Test
    @DisplayName("Testing retrieval by id")
    void getStudentAccessiblePieceByIDTest(){

        GameController controller = new GameController();
        controller.startGame(2, "test", true);
        Game game = controller.getCurrentGame();
        StudentAccessiblePiece cloud = game.getStudentAccessiblePieceByID(game.getClouds().get(0).getPieceID());
        StudentAccessiblePiece character = game.getStudentAccessiblePieceByID(game.getAllCharacters()[0].getPieceID());
        assertTrue(cloud instanceof Cloud);
        assertTrue(character instanceof Character);
    }

}