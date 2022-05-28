package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Class of the Cloud Tile, an instance of the abstract class StudentAccessiblePiece
 */
public class Cloud extends StudentAccessiblePiece implements Serializable {

    /**
     * Cloud constructor, initializes students set
     */
    public Cloud() {
        this.students = new HashSet<>();
    }
}
