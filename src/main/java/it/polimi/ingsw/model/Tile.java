package it.polimi.ingsw.model;

abstract class Tile extends StudentAccessiblePiece {
    private int tileId;
    //Set<Integer> students da StudentAccessiblePiece

    public Tile(int tileId){
        this.tileId = tileId;
    }

    public Integer getTileId() {
        return tileId;
    }
}
