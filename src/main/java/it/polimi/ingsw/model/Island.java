package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashSet;

public class Island extends Tile {
    private boolean motherNature;
    private Integer noEntry;
    //private HashSet<Integer> towers; //è più semplice tenere le due variabili sotto, non ci serve l'id. Vedi modifiche in player
    private Integer towersNumber;
    private TowerColor towersColor; //check initiation value

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

    public Island(int id){
        super(id);
        this.motherNature = false;
        this.noEntry = 0;
        this.towersNumber = 0;
        this.students = new HashSet<>();
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

    public void resolveIsland(ArrayList<Team> teams) {
        Integer maxInfluence = -2;
        Team winningTeam = new Team();
        Boolean won = false;
        Integer provisionalInfluence = -1;

        for(Team team : teams) {
            for(Player p : team.getPlayers()) {
                //if (p.getTowerColor().compareTo(this.getTowersColor()) != 0) {
                    provisionalInfluence = getPlayerInfluence(p, team.getTowerColor());
                    if (provisionalInfluence > maxInfluence) {
                        maxInfluence = provisionalInfluence;
                        winningTeam = team;
                        won = true;
                    }
                //};
            }
        }

        if(won){
            //assumes index 0 is always populated with a player having the respective tower color, even in 4 player coop
            if(this.getTowersColor() != winningTeam.getTowerColor()){
                this.setTowersColor(winningTeam.getTowerColor());

                // (TO DO) change this.towersColor to who won

                // (TO DO) Check that islands near this island have the same tower color to eventually merge them

            }
        }
    }

    public Integer getPlayerInfluence(Player player, TowerColor towerColor){
        Boolean[] professors = player.getPlayerBoard().getProfessors();
        Integer influence = 0;
        for(int i = 0; i < 5; i++){
            if(professors[i]){
                influence += Game.getStudentValue(i)*this.getStudentNumber(colorOfStudent(i));
            }
        }
        if(towerColor == this.getTowersColor()){//check if this thing works
            influence += Game.getTowerValue()*this.getTowersNumber();
        }
        return influence;
    }
    
}
