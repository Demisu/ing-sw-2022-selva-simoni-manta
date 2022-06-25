package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class for the island of the game
 */
public class Island extends StudentAccessiblePiece implements Serializable {
    private boolean motherNature;
    private Integer noEntry;
    private Integer towersNumber;
    private TowerColor towersColor; //check initiation value
    private Game game;

    /**
     * Main constructor for Islands. Sets every variable. See also StudentAccessiblePiece.StudentAccessiblePiece()
     *
     * @param game current game
     */
    public Island(Game game){
        this.motherNature = false;
        this.noEntry = 0;
        this.towersNumber = 0;
        this.students = new HashSet<>();
        this.game = game;
    }

    /**
     * Resolves the island, calculating the max influence and moving towers if needed
     *
     * @param teams teams of the game, needed for influence calculations
     */
    public void resolve(ArrayList<Team> teams) {
        Integer maxInfluence = 0;
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
            if(this.getTowersColor() != winningTeam.getTowerColor()){
                //Takes the towers (1 or more, depending on the island towersNumber) from the winning team and moves them on the island
                this.setTowersColor(winningTeam.getTowerColor());
                //If there are no towers on the island, move 1 tower. If there are 2 or more, exchange all the towers
                if(this.towersNumber == 0){
                    winningTeam.setTowerNumber(winningTeam.getTowerNumber() - 1);
                    this.towersNumber++;
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
                    winningTeam.setWinner(true);
                }
            }
        }
    }

    /**
     * Calculates the player influence and returns it. Also considers modifiers.
     *
     * @param player player to be counted as owner of the influence
     * @param towerColor color of the tower of the player (team)
     * @return The calculated player influence
     */
    public Integer getPlayerInfluence(Player player, TowerColor towerColor){
        Boolean[] professors = player.getPlayerBoard().getProfessors();
        int influence = 0;
        for(Color color : Color.values()){
            if(professors[indexOfColor(color)]){
                influence += game.getStudentValue(indexOfColor(color))*this.getStudentNumber(color);
            }
        }
        if(towerColor == this.getTowersColor()){
            influence += game.getTowerValue()*this.getTowersNumber();
        }
        return influence;
    }

    /**
     * @return towersNumber
     */
    public Integer getTowersNumber() {
        return towersNumber;
    }

    /**
     * @return noEntry number
     */
    public Integer getNoEntry() {
        return noEntry;
    }

    /**
     * @return motherNature flag
     */
    public boolean isMotherNature() {
        return motherNature;
    }

    /**
     * @return towersColor
     */
    public TowerColor getTowersColor() {
        return towersColor;
    }

    /**
     * @param towersColor towers color to be set
     */
    public void setTowersColor(TowerColor towersColor) {
        this.towersColor = towersColor;
    }

    /**
     * @param motherNature sets mother nature presence
     */
    public void setMotherNature(boolean motherNature) {
        this.motherNature = motherNature;
    }

    /**
     * @param towersNumber towers number to be set
     */
    public void setTowersNumber(Integer towersNumber) {
        this.towersNumber = towersNumber;
    }

    /**
     * @param noEntry noEntry number to be set
     */
    public void setNoEntry(Integer noEntry) {
        this.noEntry = noEntry;
    }
    
}
