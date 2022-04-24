package it.polimi.ingsw.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameControllerTest {

    @Test
    @DisplayName("Testing island managing from controller")
    void testMoveMotherNature() {

        GameController controller = new GameController();
        controller.startGame(2, "testUser");
        Integer tempID = controller.getCurrentGame().getMotherNatureIsland().getTileId();
        controller.moveMotherNature(2);

        //assertEquals(1, controller.getCurrentGame().getIslands().stream().filter(island -> island.isMotherNature()).count());
        assertNotEquals(9999, controller.getCurrentGame().getMotherNatureIsland().getTileId());
        assertEquals(tempID+2, controller.getCurrentGame().getMotherNatureIsland().getTileId());

    }
}
