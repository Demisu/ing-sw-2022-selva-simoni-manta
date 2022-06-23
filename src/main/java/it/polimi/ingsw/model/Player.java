package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for Players instances in the game
 */
public class Player implements Serializable {

    private final Integer initialCoins = 1;
    private Integer playerId;
    private Integer coins;

    private String nickname;
    private Integer teamID;
    private Boolean isActive;
    
    private ArrayList<Assistant> deck;
    private Assistant LastAssistantPlayed;
    private Boolean activeCharacter;

    private final SchoolBoard playerBoard; //package-private

    /**
     * General player constructor
     *
     * @param playerId id of the player
     */
    public Player(Integer playerId){
        this.playerId = playerId;
        this.coins = initialCoins;
        this.nickname = "Default";
        this.deck = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            deck.add(new Assistant(i, (i + i%2) / 2, i));
        }

        this.playerBoard = new SchoolBoard();
        this.activeCharacter = false;
        this.isActive = false;
    }

    /**
     * @return coins of the player
     */
    public Integer getCoins(){
        return this.coins;
    }

    /**
     * @param coins coins of the player
     */
    public void setCoins(Integer coins){
        this.coins=coins;
    }

    /**
     * @return id of player
     */
    public Integer getPlayerId() {
        return playerId;
    }

    /**
     * @param playerId id of player
     */
    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    /**
     * @return Schoolboard of the player
     */
    public SchoolBoard getPlayerBoard(){
        return this.playerBoard;
    }

    /**
     * @return nickname of the player
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * @param nickname nickname of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return Deck of player
     */
    public List<Assistant> getDeck() {
        return deck;
    }

    /**
     * @param deck Deck of player
     */
    public void setDeck(ArrayList<Assistant> deck) {
        this.deck = deck;
    }

    /**
     * @return LastAssistantPlayed
     */
    public Assistant getLastAssistantPlayed() {
        return LastAssistantPlayed;
    }

    /**
     * @return LastAssistantPlayed's TurnPriority
     */
    public Integer getLastAssistantPlayedPriority() {
        return LastAssistantPlayed.getTurnPriority();
    }

    /**
     * @param lastAssistantPlayed LastAssistantPlayed
     */
    public void setLastAssistantPlayed(Assistant lastAssistantPlayed) {
        LastAssistantPlayed = lastAssistantPlayed;
    }

    /**
     * @param status if activeCharacter
     */
    public void setActiveCharacter(Boolean status){
        this.activeCharacter = status;
    }

    /**
     * @return if activeCharacter
     */
    public Boolean hasActiveCharacter() {
        return activeCharacter;
    }

    /**
     * @return if active
     */
    public Boolean isActive() {
        return isActive;
    }

    /**
     * @param active active state of player
     */
    public void setActive(Boolean active) {
        this.isActive = active;
    }

    /**
     * @return teamID
     */
    public Integer getTeamID() {
        return teamID;
    }

    /**
     * @param teamID teamID
     */
    public void setTeamID(Integer teamID) {
        this.teamID = teamID;
    }

    /**
     * @param assistantId id of the assistant to get
     * @return assistant with input id
     */
    public Assistant getAssistantById(Integer assistantId) {

        for (Assistant assistant: deck) {
            if(assistant.getAssistantId().equals(assistantId)) {
                return assistant;
            }
        }

        System.out.println("Assistant with ID " + assistantId + " not found in " + this.nickname + "'s deck.");
        return null;
    }

    /**
     * @param assistant assistant to be played and removed
     */
    public void removeAssistant(Assistant assistant) {

        this.setLastAssistantPlayed(assistant);
        deck.remove(assistant);
    }
}
