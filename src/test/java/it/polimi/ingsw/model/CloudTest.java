package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.StudentAccessiblePiece.*;
import static org.junit.jupiter.api.Assertions.*;

import jdk.jfr.StackTrace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CloudTest {
    @Test
    @DisplayName("Testing the number of student present")
    void testGetStudentNumber() {
        Cloud cloud1 = new Cloud(0);

        cloud1.addStudent(23);
        cloud1.addStudent(21);
        cloud1.addStudent(20);
        cloud1.addStudent(7);

        assertEquals(cloud1.getStudentNumber(Color.YELLOW),4);
        assertEquals(cloud1.getStudentNumber(Color.RED),0);
        assertEquals(cloud1.getStudentNumber()[0],4);
    }
}