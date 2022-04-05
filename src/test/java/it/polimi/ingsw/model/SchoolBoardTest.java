package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.*;

class SchoolBoardTest {
    /* Check off getDiningRoomStudents */
    @Test
    @DisplayName("test setup")
    void setup() {
        SchoolBoard board = new SchoolBoard();
        board.studentToDining(1);//YELLOW
        board.studentToDining(2);//YELLOW
        board.studentToDining(3);//YELLOW
        board.studentToDining(5);//YELLOW
        board.studentToDining(40);//BLUE
        board.studentToDining(60);//GREEN
        board.studentToDining(64);//GREEN
        board.studentToDining(100);//RED
        board.studentToDining(101);//RED
        assertEquals(4,board.getDiningRoomStudents(0));
        assertEquals(1,board.getDiningRoomStudents(1));
        assertEquals(2,board.getDiningRoomStudents(2));
        assertEquals(2,board.getDiningRoomStudents(3));
        assertEquals(0,board.getDiningRoomStudents(4));
    }
}