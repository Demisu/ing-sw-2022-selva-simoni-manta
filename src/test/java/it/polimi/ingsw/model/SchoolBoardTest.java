package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SchoolBoardTest {

    @Test
    @DisplayName("Testing Students")
    void testSchoolBoardStudents() {
        SchoolBoard board = new SchoolBoard();
        board.setPieceID(1);
        board.addStudent(1);
        board.addStudent(2);
        board.addStudent(3);
        board.addStudent(5);
        board.addStudent(40);
        board.addStudent(60);
        board.addStudent(64);
        board.addStudent(100);
        board.addStudent(101);
        board.addStudent(109);
        board.studentToDining(1);//YELLOW
        board.studentToDining(2);//YELLOW
        board.studentToDining(3);//YELLOW
        board.studentToDining(5);//YELLOW
        board.studentToDining(40);//BLUE
        board.studentToDining(60);//GREEN
        board.studentToDining(64);//GREEN
        board.studentToDining(100);//RED
        board.studentToDining(101);//RED
        board.studentToDining(109);//PURPLE
        assertEquals(4,board.getDiningRoomStudents(YELLOW));
        assertEquals(1,board.getDiningRoomStudents(BLUE));
        assertEquals(2,board.getDiningRoomStudents(GREEN));
        assertEquals(2,board.getDiningRoomStudents(RED));
        assertEquals(1,board.getDiningRoomStudents(PURPLE));

        for(Color color : Color.values()){
            board.getAllDiningRoomStudents(color);
            board.getStudents(color);
        }

        board.setProfessor(GREEN, true);
        assertEquals(board.getOwnedProfessors(), 1);
    }
}