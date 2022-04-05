package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Iterator;

abstract class StudentAccessiblePiece {

    protected HashSet<Integer> students;

    public StudentAccessiblePiece(){
        HashSet<Integer> students = new HashSet<Integer>();
    }

    public void removeStudent(int student){
        students.remove(student);
    }

    public void addStudent(int student){
        students.add(student);
    }


    // Returns the number of students of enum color
    public Integer getStudentNumber(Color color) {
        /* CODIFICA COLORI
        0-25: yellow
        26-51: blue
        52-77: green
        78-103: red
        104-129: purple
        */
        return switch (color) {
            case YELLOW -> counter(0, 25);
            case BLUE -> counter(26, 51);
            case GREEN -> counter(52, 77);
            case RED -> counter(78, 103);
            case PURPLE -> counter(104, 129);
            default -> throw new IllegalArgumentException("Illegal color code passed!");
        };
    }

    // returns the corresponding student's color
    public static Color colorOfStudent(int student) {
        if(student >= 0 && student <= 25) return Color.YELLOW;
        else if(student >= 26 && student <= 51) return Color.BLUE;
        else if(student >= 52 && student <= 77) return Color.GREEN;
        else if(student >= 78 && student <= 103) return Color.RED;
        else if(student >= 104 && student <= 129) return Color.PURPLE;

        else throw new IllegalArgumentException("Illegal color code passed!");
    }

    //returns the color's chosen index to be used in Lists and Arrays (such as SchoolBoard's dining room)
    public static Integer indexOfColor(Color color) {
        return switch (color) {
            case YELLOW -> 0;
            case BLUE -> 1;
            case GREEN -> 2;
            case RED -> 3;
            case PURPLE -> 4;
            default -> throw new IllegalArgumentException("Illegal color code passed!");
        };
    }
    /* returns an array of Ints with students counted for each color
    [0] = yellow
    [1] = blue
    [2] = green
    [3] = red
    [4] = purple
    *WARN* potentially useless, also breaks from using Color enum */
    public Integer[] getStudentNumber() {
        Integer[] studentsOfEachColor = new Integer[5];

        studentsOfEachColor[0] = counter(0, 25);
        studentsOfEachColor[1] = counter(26, 51);
        studentsOfEachColor[2] = counter(52,77);
        studentsOfEachColor[3] = counter(78,103);
        studentsOfEachColor[4] = counter(104, 129);

        return studentsOfEachColor;
    }

    /* helper method for getStudentNumber, does the actual counting*/
    public Integer counter(int lowerExtreme, int upperExtreme) {
        Iterator<Integer> it = this.students.iterator();
        Integer s = 0;
        while(it.hasNext()) {
            if (it.next() >= lowerExtreme && it.next() <= upperExtreme) {
                s++;
            }
        }
        return s;
    }
}
