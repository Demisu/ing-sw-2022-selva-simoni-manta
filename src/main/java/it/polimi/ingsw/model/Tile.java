package it.polimi.ingsw.model;

abstract class Tile extends StudentAccessiblePiece {
    private Integer tileId;
    //Set<Integer> students da StudentAccessiblePiece

    public Tile(int tileId){
        this.tileId = tileId;
    }
}
