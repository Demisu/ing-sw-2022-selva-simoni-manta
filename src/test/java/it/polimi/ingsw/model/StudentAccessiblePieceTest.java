package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static org.junit.jupiter.api.Assertions.*;

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
            System.out.println("Exception verificata correttamente (StudentAccessiblePieceTest.testColorOfStudent)");
        }
    }

    @Test
    @DisplayName("Miscellanea")
    void testMiscellanea(){
        StudentAccessiblePiece piece = new Cloud();
        piece.setStudents(new HashSet<>());
        assertNotNull(piece.getStudents());
        assertEquals(StudentAccessiblePiece.parseColor("RED"), Color.RED);
        assertEquals(StudentAccessiblePiece.parseColor("YELLOW"), Color.YELLOW);
        assertEquals(StudentAccessiblePiece.parseColor("BLUE"), Color.BLUE);
        assertEquals(StudentAccessiblePiece.parseColor("GREEN"), Color.GREEN);
        assertEquals(StudentAccessiblePiece.parseColor("PURPLE"), Color.PURPLE);
        assertNull(StudentAccessiblePiece.parseColor("DHBSFIB"));
    }
}