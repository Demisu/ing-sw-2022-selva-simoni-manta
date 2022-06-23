package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.Color.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    @DisplayName("Testing mother nature managing from controller")
    void testMoveMotherNature() {

        GameController controller = new GameController();
        controller.startGame(2, "testUser", true);
        Game currentGame = controller.getCurrentGame();
        Island tempIsland = currentGame.getMotherNatureIsland();
        tempIsland.setTowersColor(TowerColor.BLACK);
        currentGame.getIslands().get( (currentGame.getIslands().indexOf(currentGame.getMotherNatureIsland()) + 2) % currentGame.getIslands().size()).setTowersColor(TowerColor.WHITE);
        int tempID = currentGame.getIslands().indexOf(tempIsland);
        controller.moveMotherNature(2);

        //assertEquals(1, controller.getCurrentGame().getIslands().stream().filter(island -> island.isMotherNature()).count());
        assertNotEquals(9999, currentGame.getIslands().indexOf(controller.getCurrentGame().getMotherNatureIsland()));
        assertEquals((tempID+2)%12, currentGame.getIslands().indexOf(controller.getCurrentGame().getMotherNatureIsland()));

    }

    @Test
    @DisplayName("Testing mother nature managing from controller 2")
    void testAnotherMoveMotherNature() {

        GameController controller = new GameController();
        controller.startGame(2, "testUser", true);
        Game currentGame = controller.getCurrentGame();
        Island tempIsland = currentGame.getMotherNatureIsland();
        Island anotherTempIsland = currentGame.getIslands().get((currentGame.getIslands().indexOf(currentGame.getMotherNatureIsland())+1) % currentGame.getIslands().size());
        anotherTempIsland.setTowersColor(TowerColor.BLACK);
        tempIsland.setTowersColor(TowerColor.BLACK);
        currentGame.getIslands().get( (currentGame.getIslands().indexOf(currentGame.getMotherNatureIsland()) + 2) % currentGame.getIslands().size()).setTowersColor(TowerColor.WHITE);
        currentGame.getIslands().get( (currentGame.getIslands().indexOf(currentGame.getMotherNatureIsland()) + 1) % currentGame.getIslands().size()).setTowersColor(TowerColor.WHITE);
        int tempID = currentGame.getIslands().indexOf(tempIsland);
        controller.moveMotherNature(2);

        //assertEquals(1, controller.getCurrentGame().getIslands().stream().filter(island -> island.isMotherNature()).count());
        assertNotEquals(9999, currentGame.getIslands().indexOf(controller.getCurrentGame().getMotherNatureIsland()));
        assertNotEquals(currentGame.getIslands().indexOf(tempIsland), currentGame.getIslands().indexOf(controller.getCurrentGame().getMotherNatureIsland()));
    }

    @Test
    @DisplayName("Testing students and merge of islands from controller")
    void testUnifyIslands(){
        GameController controller = new GameController();
        controller.startGame(2, "testUnifyIslands", true);
        Game game = controller.getCurrentGame();
        Island island0 = new Island(game);
        Island island1 = new Island(game);
        island1.setMotherNature(true);
        island0.addStudent(23);
        island0.addStudent(27);
        island1.addStudent(65);
        island1.addStudent(10);

        assertFalse(island0.getStudents().contains(65),"Student should not be here");
        assertFalse(island0.getStudents().contains(10),"Student should not be here");
        assertTrue(island1.getStudents().contains(65),"Student should be here");
        assertTrue(island1.getStudents().contains(10),"Student should be here");
        assertTrue(island1.isMotherNature());

        controller.getCurrentGame().unifyIslands(island0,island1);

        assertTrue(island0.isMotherNature());
        assertTrue(island0.getStudents().contains(65),"After merge student should be present");
        assertTrue(island0.getStudents().contains(10),"After merge student should be present");

    }

    @Test
    void testMovementSchoolBoard(){

        GameController controller = new GameController();
        //Game with 2 teams: team0 and team1
        controller.startGame(2, "testone",true);
        controller.getCurrentGame().getIslands().get(1).addStudent(13);

        //board of player 0 in team 0
        SchoolBoard test1 = controller.getCurrentGame().getTeams().get(0).getPlayers().get(0).getPlayerBoard();
        test1.setProfessor(YELLOW, true);
        test1.setProfessor(GREEN, true);
        test1.setProfessor(BLUE, true);
        test1.setProfessor(PURPLE, true);
        test1.setProfessor(RED, true);

        //board of player 0 in team 1
        SchoolBoard test2 = controller.getCurrentGame().getTeams().get(1).getPlayers().get(0).getPlayerBoard();

        controller.moveStudent(13, controller.getCurrentGame().getIslands().get(1).getPieceID(), controller.getCurrentGame().getIslands().get(2).getPieceID());
        controller.moveProfessor(YELLOW, test1.getPieceID(), test2.getPieceID());
        controller.moveProfessor(GREEN, test1.getPieceID(), test2.getPieceID());
        controller.moveProfessor(BLUE, test1.getPieceID(), test2.getPieceID());
        controller.moveProfessor(PURPLE, test1.getPieceID(), test2.getPieceID());
        controller.moveProfessor(RED, test1.getPieceID(), test2.getPieceID());

        Boolean[] testone = test2.getProfessors();

        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(YELLOW))]);
        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(GREEN))]);
        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(BLUE))]);
        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(PURPLE))]);
        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(RED))]);

    }

    @Test
    @DisplayName("Testing add of players in the game")
    void testAddingPlayer(){
        GameController controller = new GameController();
        controller.startGame(2, "xX-KEKKER2000-Xx", true);
        controller.addPlayer("sus");
        assertEquals(controller.getPlayerNumber(),2);

    }

    @Test
    @DisplayName("Testing modifiers")
    void testModifiers(){
        GameController controller = new GameController();
        controller.startGame(2,"Gianfranco", true);
        Game gameTest = controller.getCurrentGame();
        gameTest.addPlayer("Frank");
        gameTest.resetModifiers();
        assertEquals(gameTest.getTowerValue(),1);
    }

    @Test
    @DisplayName("Testing professor updater")
    void checkProfessorChangeTest(){
        GameController controller = new GameController();
        controller.startGame(2, "testProf", true);
        controller.checkProfessorChange(BLUE);
        controller.checkProfessorChange(YELLOW);
        controller.checkProfessorChange(GREEN);
        controller.checkProfessorChange(RED);
        controller.checkProfessorChange(PURPLE);
    }

    @Test
    @DisplayName("Testing player disconnection and turn skip")
    void testSkip(){
        GameController controller = new GameController();
        controller.setSkipTurnDelay(0);
        controller.startGame(2, "testChar", true);
        String previous = controller.getCurrentGame().getCurrentPlayer();
        controller.setInactive(controller.getCurrentGame().getCurrentPlayer());
        for(int i = 0; i < Integer.MAX_VALUE; i++){
            i++;
            i--;
        }
        controller.addPlayer("testChar");
        assertNotEquals(previous, controller.getCurrentGame().getCurrentPlayer());
        assertFalse(controller.getCurrentGame().getPlayerByNickname(controller.getCurrentGame().getCurrentPlayer()).isActive());
    }

    @Test
    @DisplayName("Testing movement in schoolboard")
    void checkMovementSchoolboard(){
        GameController gameController = new GameController();
        gameController.startGame(2, "test", true);
        Game game = gameController.getCurrentGame();
        Player creator = game.getPlayers().get(0);
        int amount = creator.getPlayerBoard().getStudents().size();
        while(!creator.getPlayerBoard().getStudents().isEmpty()) {
            Integer student = creator.getPlayerBoard().getStudents().stream().toList().get(0);
            Integer playerboard = creator.getPlayerBoard().getPieceID();
            System.out.println("Moving student " + student + " in schoolboard " + playerboard);
            gameController.moveStudent(student, playerboard, playerboard);
        }
        int numberOfMovedStudents = 0;
        for(Color color : Color.values()){
            numberOfMovedStudents += creator.getPlayerBoard().getDiningRoomStudents(color);
        }
        assertTrue(numberOfMovedStudents > 0);
        assertEquals(amount, numberOfMovedStudents);
    }

    @Test
    @DisplayName("Testing character use")
    void testPlayCharacter(){
        GameController controller = new GameController();
        controller.startGame(2, "testChar", true);
        ArrayList<Character> charactersNew = new ArrayList<>(Arrays.asList(controller.getCurrentGame().getAllCharacters()));
        Character testing = new Character();
        testing.setEffectType("wrong");
        charactersNew.add(testing);
        Character[] arrayChar = new Character[4];
        charactersNew.toArray(arrayChar);
        controller.getCurrentGame().setAvailableCharacters(arrayChar);
        PlayCharacterRequest testReq = new PlayCharacterRequest(3, "testChar");
        controller.playCharacter(testReq);
        controller.playCharacter(testReq);
    }

}
