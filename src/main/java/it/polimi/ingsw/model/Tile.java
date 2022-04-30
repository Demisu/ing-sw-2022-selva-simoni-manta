package it.polimi.ingsw.model;

import java.util.HashSet;

abstract class Tile extends StudentAccessiblePiece {
    private int tileId;
    //Set<Integer> students da StudentAccessiblePiece

    public Tile(int tileId){
        super();
        this.tileId = tileId;

    }

    public Integer getTileId() {
        return tileId;
    }
}
