package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Iterator;

public class Island extends Tile {
    private boolean motherNature;
    private Integer noEntry;
    //private HashSet<Integer> towers; //è più semplice tenere le due variabili sotto, non ci serve l'id. Vedi modifiche in player
    private Integer towersNumber;

    public Integer getTowersNumber() {
        return towersNumber;
    }

    public void setTowersNumber(Integer towersNumber) {
        this.towersNumber = towersNumber;
    }

    public TowerColor getTowersColor() {
        return towersColor;
    }

    public void setTowersColor(TowerColor towersColor) {
        this.towersColor = towersColor;
    }

    private TowerColor towersColor; //check initiation value

    public Island(int id){
        super(id);
    }

    public boolean isMotherNature() {
        return motherNature;
    }

    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }

    public Integer getNoEntry() {
        return noEntry;
    }

    public void setNoEntry(Integer noEntry) {
        this.noEntry = noEntry;
    }

    /* returns an array of Ints with students counted for each color
    [0] = yellow
    [1] = blue
    [2] = green
    [3] = red
    [4] = purple
    *WARN* potentially useless, also breaks from using Color enum */
    public void resolveIsland(){

        Integer currentInfluence = 0;
        Integer maxInfluence = -1;
        Integer winningTeamIndex = -1;

        for(int i = 0; i < Game.getTeamsNumber(); i++){
            currentInfluence = 0;
            for(Player player : Game.getTeams().get(i)){
                currentInfluence += this.getPlayerInfluence(player);
            }
            if(currentInfluence > maxInfluence){
                maxInfluence = currentInfluence;
                winningTeamIndex = i;
            }
        }
        if(winningTeamIndex != -1){
            if(this.getTowersColor() != Game.getTeams().get(winningTeamIndex).getTowerColor()){
                this.setTowersColor(Game.getTeams().get(winningTeamIndex).getTowerColor());

                // (TO DO) change this.towersColor to who won

                // (TO DO) Check that islands near this island have the same tower color to eventually merge them

            }
        }
    }

    public Integer getPlayerInfluence(Player player){
        Boolean[] professors = player.playerBoard.getProfessors();
        Integer influence = 0;
        for(int i = 0; i < 5; i++){
            if(professors[i]){
                influence += Game.getStudentValue(i)*this.getStudentNumber(i);//CONTROLLARE CHE NON DIA PROBLEMI USARE IL NUMERO AL POSTO DI Color color

            }
        }
        if(player.getTowerColor() == this.getTowerColor()){//check if this thing works
            influence += Game.getTowerValue()*this.getTowersNumber();
        }
        return influence;
    }
    
}
