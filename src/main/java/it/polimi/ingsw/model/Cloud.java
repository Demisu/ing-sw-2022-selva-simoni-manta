package it.polimi.ingsw.model;

import java.util.HashSet;

public class Cloud extends Tile {
    
    public Cloud(int id) {
        super(id);
        this.students = new HashSet<>();

    }
}
