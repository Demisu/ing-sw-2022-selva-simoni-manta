package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashSet;

public class SchoolBoard extends StudentAccessiblePiece {
    //inherits HashSet<Integer> students, which is used to save students
    //in front of the main entrance;
    private ArrayList<Integer> yellowDiningRoomStudents;
    private ArrayList<Integer> blueDiningRoomStudents;
    private ArrayList<Integer> greenDiningRoomStudents;
    private ArrayList<Integer> redDiningRoomStudents;
    private ArrayList<Integer> purpleDiningRoomStudents;

    private Boolean[] professors;

    //moves student from entrance to dining
    //BY THE RULES, THIS ACTION CANNOT BE UNDONE! (only by one Character)
    public void studentToDining(Integer student) {
        this.students.remove(student);
        switch(colorOfStudent(student)){
            case YELLOW:
                this.yellowDiningRoomStudents.add(student);
                break;
            case BLUE:
                this.blueDiningRoomStudents.add(student);
                break;
            case GREEN:
                this.greenDiningRoomStudents.add(student);
                break;
            case RED:
                this.redDiningRoomStudents.add(student);
                break;
            case PURPLE:
                this.purpleDiningRoomStudents.add(student);
                break;
        }
    }
    public Integer getDiningRoomStudents(Color color){
        switch(color){
            case YELLOW:
                return this.yellowDiningRoomStudents.size();
            case BLUE:
                return this.blueDiningRoomStudents.size();
            case GREEN:
                return this.greenDiningRoomStudents.size();
            case RED:
                return this.redDiningRoomStudents.size();
            case PURPLE:
                return this.purpleDiningRoomStudents.size();
            default:
                return -69;
        }
    }
    //sets professor on the schoolboard
    public void setProfessor(Color color, boolean status) {
        switch(color) {
            case YELLOW: {
                this.professors[0] = status;
                break;
            }
            case BLUE: {
                this.professors[1] = status;
                break;
            }

            case GREEN: {
                this.professors[2] = status;
                break;
            }

            case RED: {
                this.professors[3] = status;
                break;
            }

            case PURPLE: {
                this.professors[4] = status;
                break;
            }
        }
        //dovrebbe anche disattivare questo professor sulle altre board?
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

}
