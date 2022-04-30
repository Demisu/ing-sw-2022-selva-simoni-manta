package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private TowerColor towerColor;
    private Integer towerNumber;//Towers in player's schoolboard (not island)
    //Check that the number doesn't go below zero.
    private List<Player> players;
    private Integer teamId;
    public Team(TowerColor towerColor, Integer towerNumber, Integer teamId){
        this.towerColor = towerColor;
        this.towerNumber = towerNumber;
        players = new ArrayList<>();
        this.teamId = teamId;
    }
    public Team(){
        this.teamId = -1;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public Integer getTowerNumber() {
        return towerNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public void setTowerNumber(Integer towerNumber) {
        this.towerNumber = towerNumber;
    }
}
