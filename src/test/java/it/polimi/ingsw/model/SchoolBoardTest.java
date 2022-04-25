package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static it.polimi.ingsw.model.Color.*;
import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.*;

class SchoolBoardTest {
    /* Check off getDiningRoomStudents */
    @Test
    @DisplayName("Testing Students")
    void testSchoolBoardStudents() {
        SchoolBoard board = new SchoolBoard();
        board.addStudent(1);
        board.addStudent(2);
        board.addStudent(3);
        board.addStudent(5);
        board.addStudent(40);
        board.addStudent(60);
        board.addStudent(64);
        board.addStudent(100);
        board.addStudent(101);
        board.studentToDining(1);//YELLOW
        board.studentToDining(2);//YELLOW
        board.studentToDining(3);//YELLOW
        board.studentToDining(5);//YELLOW
        board.studentToDining(40);//BLUE
        board.studentToDining(60);//GREEN
        board.studentToDining(64);//GREEN
        board.studentToDining(100);//RED
        board.studentToDining(101);//RED
        assertEquals(4,board.getDiningRoomStudents(YELLOW));
        assertEquals(1,board.getDiningRoomStudents(BLUE));
        assertEquals(2,board.getDiningRoomStudents(GREEN));
        assertEquals(2,board.getDiningRoomStudents(RED));
        assertEquals(0,board.getDiningRoomStudents(PURPLE));
    }
}