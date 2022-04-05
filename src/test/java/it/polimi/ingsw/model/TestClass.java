package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TestClass {

//    @BeforeEach
    
    //@RepeatedTest(5)
    @Test
    @DisplayName("Testing the appropriate return index of colors")
    void testGetAStudent() {
        assertEquals(0, indexOfColor(Color.YELLOW), "Index 0 should correspond to yellow");
        assertEquals(1, indexOfColor(Color.BLUE), "Index 1 should correspond to blue");
        assertEquals(2, indexOfColor(Color.RED), "Index 2 should correspond to red");
        assertEquals(3, indexOfColor(Color.GREEN), "Index 3 should correspond to green");
        assertEquals(4, indexOfColor(Color.PURPLE), "Index 4 should correspond to purple");
    }
    

}