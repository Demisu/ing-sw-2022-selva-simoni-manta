package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.HashSet;

public class Cloud extends StudentAccessiblePiece implements Serializable {
    
    public Cloud() {
        this.students = new HashSet<>();
    }
}
