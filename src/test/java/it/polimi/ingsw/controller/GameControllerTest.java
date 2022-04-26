package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.model.StudentAccessiblePiece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Color.YELLOW;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    @Test
    @DisplayName("Testing mother nature managing from controller")
    void testMoveMotherNature() {

        GameController controller = new GameController();
        controller.startGame(2, "testUser");
        Integer tempID = controller.getCurrentGame().getMotherNatureIsland().getTileId();
        controller.moveMotherNature(2);

        //assertEquals(1, controller.getCurrentGame().getIslands().stream().filter(island -> island.isMotherNature()).count());
        assertNotEquals(9999, controller.getCurrentGame().getMotherNatureIsland().getTileId());
        assertEquals(tempID+2, controller.getCurrentGame().getMotherNatureIsland().getTileId());

    }

    @Test
    @DisplayName("Testing students and merge of islands from controller")
    void testUnifyIslands(){
        GameController controller = new GameController();
        controller.startGame(2, "testUnifyIslands");
        Island island0 = new Island(0);
        Island island1 = new Island(1);
        island0.addStudent(23);
        island0.addStudent(27);
        island1.addStudent(65);
        island1.addStudent(10);

        assertFalse(island0.getStudents().contains(65),"Student should not be here");
        assertFalse(island0.getStudents().contains(10),"Student should not be here");
        assertTrue(island1.getStudents().contains(65),"Student should be here");
        assertTrue(island1.getStudents().contains(10),"Student should be here");

        controller.unifyIslands(island0,island1);

        assertTrue(island0.getStudents().contains(65),"After merge student should be present");
        assertTrue(island0.getStudents().contains(10),"After merge student should be present");

    }

    @Test
    void testMovement(){

        GameController controller = new GameController();
        controller.startGame(2, "testone");
        controller.getCurrentGame().getIslands().get(1).addStudent(13);
        SchoolBoard test1 = new SchoolBoard();
        test1.setProfessor(YELLOW, true);
        SchoolBoard test2 = new SchoolBoard();

        controller.moveStudent(13, controller.getCurrentGame().getIslands().get(1), controller.getCurrentGame().getIslands().get(2));
        controller.moveProfessor(YELLOW, test1, test2);
        Boolean[] testone = new Boolean[5];

        assertTrue(testone[(StudentAccessiblePiece.indexOfColor(YELLOW))]);

    }
}
