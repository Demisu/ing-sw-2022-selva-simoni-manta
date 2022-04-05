package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashSet;

public class SchoolBoard extends StudentAccessiblePiece {
    //inherits HashSet<Integer> students, which is used to save students
    //in front of the main entrance;
    private ArrayList<HashSet<Integer>> diningRoomStudents;
    private Boolean[] professors;

    //moves student from entrance to dining
    //BY THE RULES, THIS ACTION CANNOT BE UNDONE! (only by one Character)
    public void studentToDining(int student) {
        this.students.remove(student);
        this.diningRoomStudents.get(indexOfColor(colorOfStudent(student))).add(student);
    }
    public Integer getDiningRoomStudents(int color){
        return this.diningRoomStudents.get(color).size();
    }
    //sets professor on the schoolboard
    public void setProfessorOfColor(Color color, boolean status) {
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
        this.diningRoomStudents = new ArrayList<>();
        this.professors = new Boolean[5];
    }

}
