package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class SchoolBoard extends StudentAccessiblePiece implements Serializable {
    //inherits HashSet<Integer> students, which is used to save students
    //in front of the main entrance;
    private ArrayList<Integer> yellowDiningRoomStudents;
    private ArrayList<Integer> blueDiningRoomStudents;
    private ArrayList<Integer> greenDiningRoomStudents;
    private ArrayList<Integer> redDiningRoomStudents;
    private ArrayList<Integer> purpleDiningRoomStudents;

    private Boolean[] professors;

    public SchoolBoard() {
        super();
        this.yellowDiningRoomStudents = new ArrayList<>();
        this.blueDiningRoomStudents = new ArrayList<>();
        this.greenDiningRoomStudents = new ArrayList<>();
        this.redDiningRoomStudents = new ArrayList<>();
        this.purpleDiningRoomStudents = new ArrayList<>();
        this.professors = new Boolean[5];
        this.students = new HashSet<>();
    }

    //moves student from entrance to dining
    //BY THE RULES, THIS ACTION CANNOT BE UNDONE! (only by one Character)
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
    public Integer getDiningRoomStudents(Color color){
        return switch (color) {
            case YELLOW -> this.yellowDiningRoomStudents.size();
            case BLUE -> this.blueDiningRoomStudents.size();
            case GREEN -> this.greenDiningRoomStudents.size();
            case RED -> this.redDiningRoomStudents.size();
            case PURPLE -> this.purpleDiningRoomStudents.size();
        };
    }
    //sets professor on the schoolboard
    public void setProfessor(Color color, boolean status) {
        switch (color) {
            case YELLOW -> {
                this.professors[0] = status;
            }
            case BLUE -> {
                this.professors[1] = status;
            }
            case GREEN -> {
                this.professors[2] = status;
            }
            case RED -> {
                this.professors[3] = status;
            }
            case PURPLE -> {
                this.professors[4] = status;
            }
        }
    }

    public Boolean[] getProfessors() {
        /* returns an array of Booleans with students counted for each color
        [0] = yellow
        [1] = blue
        [2] = green
        [3] = red
        [4] = purple
        *WARN* potentially useless, also breaks from using Color enum */
        return this.professors;
    }
}
