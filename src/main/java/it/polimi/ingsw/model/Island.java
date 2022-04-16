package it.polimi.ingsw.model;

import java.util.ArrayList;

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


    public void resolveIsland(ArrayList<ArrayList<Player>> teams) {
        Integer maxInfluence = -2;
        Integer winningTeamIndex = -1;
        Integer provisionalInfluence = -1;

        for(ArrayList<Player> team : teams) {
            for(Player p : team) {
                //if (p.getTowerColor().compareTo(this.getTowersColor()) != 0) {
                    provisionalInfluence = getPlayerInfluence(p);
                    if (provisionalInfluence > maxInfluence) {
                        maxInfluence = provisionalInfluence;
                        winningTeamIndex = teams.indexOf(team);
                    }
                //};
            }
        }

        if(winningTeamIndex != -1){
            //assumes index 0 is always populated with a player having the respective tower color, even in 4 player coop
            if(this.getTowersColor() != teams.get(winningTeamIndex).get(0).getTowerColor()){
                this.setTowersColor(teams.get(winningTeamIndex).get(0).getTowerColor());

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
                influence += Game.getStudentValue(i)*this.getStudentNumber(colorOfStudent(i));
            }
        }
        if(player.getTowerColor() == this.getTowersColor()){//check if this thing works
            influence += Game.getTowerValue()*this.getTowersNumber();
        }
        return influence;
    }
    
}
