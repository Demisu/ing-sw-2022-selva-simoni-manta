package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.StudentAccessiblePiece.*;
import static org.junit.jupiter.api.Assertions.*;

import jdk.jfr.StackTrace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StudentAccessiblePieceTest {
    @Test
    @DisplayName("Testing the appropriate return index of colors")
    void testIndexOfColor() {
        assertEquals(0, indexOfColor(Color.YELLOW), "Index 0 should correspond to yellow");
        assertEquals(1, indexOfColor(Color.BLUE), "Index 1 should correspond to blue");
        assertEquals(2, indexOfColor(Color.GREEN), "Index 2 should correspond to green");
        assertEquals(3, indexOfColor(Color.RED), "Index 3 should correspond to red");
        assertEquals(4, indexOfColor(Color.PURPLE), "Index 4 should correspond to purple");
    }
    @Test
    @DisplayName("Testing the appropriate return color of students")
    void testColorOfStudent() {
        assertEquals(Color.YELLOW, colorOfStudent(23), "23's color should correspond to yellow");
        assertEquals(Color.BLUE, colorOfStudent(42), "42's color should correspond to blue");
        assertEquals(Color.GREEN, colorOfStudent(77), "77's color should correspond to green");
        assertEquals(Color.RED, colorOfStudent(78), "78's color should correspond to red");
        assertEquals(Color.PURPLE, colorOfStudent(129), "129's color should correspond to purple");
        Color color;
        try{
            color=colorOfStudent(150);
        }catch(IllegalArgumentException exe){
            exe.printStackTrace();
        }
    }
}