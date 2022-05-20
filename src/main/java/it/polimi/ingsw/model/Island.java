package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class Island extends StudentAccessiblePiece implements Serializable {
    private boolean motherNature;
    private Integer noEntry;
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

    public Island(){
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

    public void resolve(ArrayList<Team> teams) {
        Integer maxInfluence = -2;
        Team winningTeam = new Team();
        boolean won = false;
        Integer provisionalInfluence = -1;

        for(Team team : teams) {
            for(Player p : team.getPlayers()) {
                provisionalInfluence = getPlayerInfluence(p, team.getTowerColor());
                if (provisionalInfluence > maxInfluence) {
                    maxInfluence = provisionalInfluence;
                    winningTeam = team;
                    won = true;
                }
            }
        }

        if(won){
            //assumes index 0 is always populated with a player having the respective tower color, even in 4 player coop
            if(this.getTowersColor() != winningTeam.getTowerColor()){
                this.setTowersColor(winningTeam.getTowerColor());
                winningTeam.setTowerNumber(winningTeam.getTowerNumber() - 1);
            }
        }
    }

    public Integer getPlayerInfluence(Player player, TowerColor towerColor){
        Boolean[] professors = player.getPlayerBoard().getProfessors();
        int influence = 0;
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
