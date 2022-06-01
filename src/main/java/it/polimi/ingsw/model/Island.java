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
        Integer provisionalInfluence;

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
                //Takes the towers (1 or more, depending on the island towersNumber) from the winning team and moves them on the island
                this.setTowersColor(winningTeam.getTowerColor());
                //If there are no towers on the island, move 1 tower. If there are 2 or more, exchange all the towers
                if(this.towersNumber == 0){
                    winningTeam.setTowerNumber(winningTeam.getTowerNumber() - 1);
                } else {
                    winningTeam.setTowerNumber(winningTeam.getTowerNumber() - this.towersNumber);
                    //Gives back the losing team their towers
                    for (Team team : teams){
                        //If this team owns the towers
                        if(team.getTowerColor().equals(this.towersColor)){
                            team.setTowerNumber(team.getTowerNumber() + this.towersNumber);
                        }
                    }
                }
                //Check if winning team has no more towers, if so they win
                if(winningTeam.getTowerNumber() <= 0){

                    //TODO GAME ENDS

                }
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
