package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    @DisplayName("Testing student movement from the game bag and the appropriate return index of colors")
    void testGetAStudent() {

        Game testGame = new Game(2, "test");
        ArrayList<Integer> testStudents = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++) {
            testStudents.add(testGame.getAStudent());
        }

        assertEquals(10, testStudents.size());
        assertEquals(120, testGame.getBagStudents().size());

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

        Game testGame = new Game(2, "test");

        assertNull(testGame.getPlayerById(3));
        assertTrue(testGame.getPlayers().contains(testGame.getPlayerById(1)));
        assertTrue(testGame.getPlayers().contains(testGame.getPlayerById(0)));

    }

    @Test
    @DisplayName("Testing player recovery by ID")
    void testGetPlayers(){

        Game testGame = new Game(2, "test");

        assertEquals(testGame.getPlayers().size(), 2);

    }
}