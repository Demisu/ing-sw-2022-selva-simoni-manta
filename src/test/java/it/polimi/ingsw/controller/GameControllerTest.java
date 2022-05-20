package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Color.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    @DisplayName("Testing mother nature managing from controller")
    void testMoveMotherNature() {

        GameController controller = new GameController();
        controller.startGame(2, "testUser");
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
        controller.startGame(2, "testUser");
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
        controller.startGame(2, "testUnifyIslands");
        Island island0 = new Island();
        Island island1 = new Island();
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
        controller.startGame(2, "testone");
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
        controller.startGame(2, "xX-KEKKER2000-Xx");
        controller.addPlayer("sus");
        assertEquals(controller.getPlayerNumber(),2);

    }


}
