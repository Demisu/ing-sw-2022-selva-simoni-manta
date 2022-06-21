package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.fill;

/**
 * Class for School Boards,
 * inherits HashSetstudents from StudentAccessiblePiece, which is used to save students in the entrance
 */
public class SchoolBoard extends StudentAccessiblePiece implements Serializable {

    private ArrayList<Integer> yellowDiningRoomStudents;
    private ArrayList<Integer> blueDiningRoomStudents;
    private ArrayList<Integer> greenDiningRoomStudents;
    private ArrayList<Integer> redDiningRoomStudents;
    private ArrayList<Integer> purpleDiningRoomStudents;

    private Boolean[] professors;

    /**
     * SchoolBoard constructor, initializes dining room students and professors
     */
    public SchoolBoard() {
        super();
        this.yellowDiningRoomStudents = new ArrayList<>();
        this.blueDiningRoomStudents = new ArrayList<>();
        this.greenDiningRoomStudents = new ArrayList<>();
        this.redDiningRoomStudents = new ArrayList<>();
        this.purpleDiningRoomStudents = new ArrayList<>();
        this.professors = new Boolean[5];
        fill(professors, false);
        this.students = new HashSet<>();
    }

    /**
     * Moves the student from entrance to dining
     *
     * @param student student to move
     */
    public void studentToDining(Integer student) {
        this.students.remove(student);
        switch (colorOfStudent(student)) {
            case YELLOW -> this.yellowDiningRoomStudents.add(student);
            case BLUE -> this.blueDiningRoomStudents.add(student);
            case GREEN -> this.greenDiningRoomStudents.add(student);
            case RED -> this.redDiningRoomStudents.add(student);
            case PURPLE -> this.purpleDiningRoomStudents.add(student);
        }
    }

    /**
     * @param color color of the students
     * @return number of input color students in the dining room
     */
    public Integer getDiningRoomStudents(Color color){
        return switch (color) {
            case YELLOW -> this.yellowDiningRoomStudents.size();
            case BLUE -> this.blueDiningRoomStudents.size();
            case GREEN -> this.greenDiningRoomStudents.size();
            case RED -> this.redDiningRoomStudents.size();
            case PURPLE -> this.purpleDiningRoomStudents.size();
        };
    }

    /**
     * @param color color of the students to get
     * @return ArrayList containing all the students of this color inside the dining room
     */
    public ArrayList<Integer> getAllDiningRoomStudents(Color color){
        return switch (color) {
            case YELLOW -> this.yellowDiningRoomStudents;
            case BLUE -> this.blueDiningRoomStudents;
            case GREEN -> this.greenDiningRoomStudents;
            case RED -> this.redDiningRoomStudents;
            case PURPLE -> this.purpleDiningRoomStudents;
        };
    }

    /**
     * Sets professor status on the school board
     *
     * @param color color of the professor
     * @param status status of the professor (true = owned, false = not owned)
     */
    public void setProfessor(Color color, boolean status) {
        switch (color) {
            case YELLOW -> this.professors[0] = status;
            case BLUE -> this.professors[1] = status;
            case GREEN -> this.professors[2] = status;
            case RED -> this.professors[3] = status;
            case PURPLE -> this.professors[4] = status;
        }
    }

    /**
     * @return array of Booleans with professors' status in this SchoolBoard
     */
    public Boolean[] getProfessors() {
        return this.professors;
    }

    /**
     * @return number of owned professors
     */
    public Integer getOwnedProfessors(){

        Integer owned = 0;
        for (Boolean flag : professors) {
            if(flag){
                owned++;
            }
        }

        return owned;
    }
}
