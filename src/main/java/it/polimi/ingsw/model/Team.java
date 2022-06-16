package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Teams participating in the game
 */
public class Team implements Serializable {
    private TowerColor towerColor;
    private Integer towerNumber;//Towers in player's school board (not island)
    //Check that the number doesn't go below zero.
    private List<Player> players;
    private final Integer teamId;
    private Boolean isWinner = false;

    /**
     * @param towerColor tower color of the team
     * @param towerNumber tower number of the team, depends on player number
     * @param teamId ID of the team
     */
    public Team(TowerColor towerColor, Integer towerNumber, Integer teamId){
        this.towerColor = towerColor;
        this.towerNumber = towerNumber;
        players = new ArrayList<>();
        this.teamId = teamId;
    }

    /**
     * Used only for testing,
     * custom constructor to be avoided in other circumstances
     */
    public Team(){
        this.teamId = -1;
    }

    /**
     * @return Team's unique ID
     */
    public Integer getTeamId() {
        return teamId;
    }

    /**
     * @return Team's tower color
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * @return Team's available tower number
     */
    public Integer getTowerNumber() {
        return towerNumber;
    }

    /**
     * @return Team's players list (1 or 2 players)
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @param player player to be added to the team
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * @param towerColor color of towers to be assigned to the team
     */
    public void setTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    /**
     * @param towerNumber number of towers available for the team
     */
    public void setTowerNumber(Integer towerNumber) {
        this.towerNumber = towerNumber;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public Boolean isWinner(){
        return isWinner;
    }

    public Integer getProfessorsNumber(){

        Integer profNumber = 0;
        for (Player player : players) {
            profNumber += player.getPlayerBoard().getOwnedProfessors();
        }

        return profNumber;
    }
}
