package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.Color.*;

/**
 * Abstract class that includes all pieces which can contain students (Islands, Clouds, etc.)
 */
public abstract class StudentAccessiblePiece implements Serializable {

    /**
     * students present in this piece
     */
    protected HashSet<Integer> students;
    private Integer pieceID;

    /**
     * Constructor of StudentAccessiblePiece,
     * gives a unique id to every piece created
     */
    public StudentAccessiblePiece(){
        HashSet<Integer> students = new HashSet<>();
        pieceID = Game.getNextPieceID();
    }

    /**
     * @return students present on the piece
     */
    public HashSet<Integer> getStudents() {
        return students;
    }

    /**
     * @param student student to be removed from the piece
     */
    public void removeStudent(Integer student){
        students.remove(student);
    }

    /**
     * @param student student to be added to the piece
     */
    public void addStudent(Integer student){
        students.add(student);
    }

    /**
     * StudentID to Color conversion:
     * 0-25: yellow
     * 26-51: blue
     * 52-77: green
     * 78-103: red
     * 104-129: purple
     *
     * @param color color of the students to get
     * @return the number of students of that color
     */
    public Integer getStudentNumber(Color color) {
        return switch (color) {
            case YELLOW -> counter(0, 25);
            case BLUE -> counter(26, 51);
            case GREEN -> counter(52, 77);
            case RED -> counter(78, 103);
            case PURPLE -> counter(104, 129);
        };
    }

    /**
     * @param color color of students to get
     * @return List of all the students of that color contained in the piece
     */
    public List<Integer> getStudents(Color color) {
        return students.stream().filter(student -> colorOfStudent(student).equals(color)).collect(Collectors.toList());
    }

    /**
     * StudentID to Color conversion:
     * 0-25: yellow
     * 26-51: blue
     * 52-77: green
     * 78-103: red
     * 104-129: purple
     *
     * @param student student unique ID
     * @return the color of that student
     */
    public static Color colorOfStudent(int student) {
        if(student >= 0 && student <= 25) return Color.YELLOW;
        else if(student >= 26 && student <= 51) return Color.BLUE;
        else if(student >= 52 && student <= 77) return Color.GREEN;
        else if(student >= 78 && student <= 103) return Color.RED;
        else if(student >= 104 && student <= 129) return Color.PURPLE;

        else throw new IllegalArgumentException("Illegal color code passed!");
    }

    /**
     * @param color color of which you want the index
     * @return color's index to be used in Arrays (such as SchoolBoard's dining room)
     */
    public static Integer indexOfColor(Color color) {
        return switch (color) {
            case YELLOW -> 0;
            case BLUE -> 1;
            case GREEN -> 2;
            case RED -> 3;
            case PURPLE -> 4;
        };
    }

    /**
     * @param color color string to parse
     * @return Color corresponding to the string, null if invalid
     */
    public static Color parseColor(String color){
        return switch (color) {
            case "YELLOW" -> YELLOW;
            case "BLUE" -> BLUE;
            case "GREEN" -> GREEN;
            case "RED" -> RED;
            case "PURPLE" -> PURPLE;
            default -> null;
        };
    }

    /**
     * Indexes to Color:
     * [0] = yellow
     * [1] = blue
     * [2] = green
     * [3] = red
     * [4] = purple
     *
     * @return an array of Integers with students counted for each color
     */
    public Integer[] getStudentNumber() {
        Integer[] studentsOfEachColor = new Integer[5];

        studentsOfEachColor[0] = counter(0, 25);
        studentsOfEachColor[1] = counter(26, 51);
        studentsOfEachColor[2] = counter(52,77);
        studentsOfEachColor[3] = counter(78,103);
        studentsOfEachColor[4] = counter(104, 129);

        return studentsOfEachColor;
    }

    /**
     * Helper method for getStudentNumber, does the actual counting
     *
     * @param lowerExtreme first ID with a certain color
     * @param upperExtreme last ID of the same color
     * @return the student count needed
     */
    public int counter(int lowerExtreme, int upperExtreme) {
        int s = 0;

        for (Integer student : this.students) {
            if(student >= lowerExtreme && student <= upperExtreme){
                s++;
            }
        }
        return s;

    }

    /**
     * @return Unique piece ID
     */
    public Integer getPieceID() {
        return pieceID;
    }

    /**
     * Used for testing,
     * avoid using this for other purposes
     *
     * @param pieceID piece ID that overwrites the old ID
     */
    public void setPieceID(Integer pieceID) {
        this.pieceID = pieceID;
    }

    /**
     * @param students students to be set as content
     */
    public void setStudents(HashSet<Integer> students) {
        this.students = students;
    }
}
