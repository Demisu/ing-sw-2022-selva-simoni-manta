package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.model.Assistant;

public class Player implements Serializable {

    private final Integer initialCoins = 1;
    private Integer playerId;
    private Integer coins;

    //public TowerColor getTowerColor() {
    //    return towerColor;
    //}
//
    //public void setTowerColor(TowerColor towerColor) {
    //    this.towerColor = towerColor;
    //}

    private String nickname;
    //private TowerColor towerColor;
    //private Integer towerNumber;//Towers in player's schoolboard (not island)
    //Check that the number doesn't go below zero. 
    
    private ArrayList<Assistant> deck;
    private Assistant LastAssistantPlayed;
    private Boolean activeCharacter;

    SchoolBoard playerBoard; //package-private

    public Player(Integer playerId){
        this.playerId = playerId;
        this.coins = initialCoins;
        this.nickname = "Default";
    //    this.towerColor = towerColor;
    //    this.towerNumber = towerNumber;
        this.deck = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            deck.add(new Assistant(i, 10-i, Game.getNextPieceID()));
        }

        this.playerBoard = new SchoolBoard();
        this.activeCharacter = false;
    }

    public Integer getCoins(){
        return this.coins;
    }

    public void setCoins(Integer coins){
        this.coins=coins;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerid(Integer playerid) {
        this.playerId = playerid;
    } //Useful only to change existing PlayerId in case it is needed.

    public SchoolBoard getPlayerBoard(){
        return this.playerBoard;
    }

    public String getNickname(){
        return this.nickname;
    }

    public void setNickname(String nicknameOfCreator) {
        this.nickname = nicknameOfCreator;
    }

    public List<Assistant> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Assistant> deck) {
        this.deck = deck;
    }

    public Assistant getLastAssistantPlayed() {
        return LastAssistantPlayed;
    }

    public void setLastAssistantPlayed(Assistant lastAssistantPlayed) {
        LastAssistantPlayed = lastAssistantPlayed;
    }

    public void setActiveCharacter(Boolean status){
        this.activeCharacter = status;
    }

    public Boolean hasActiveCharacter() {
        return activeCharacter;
    }

    public Assistant getAssistantById(Integer assistantId) {

        for (Assistant assistant: deck) {
            if(assistant.getAssistantId() == assistantId) {
                return assistant;
            }
        }

        System.out.println("Assistant with ID " + assistantId + " not found in " + this.nickname + "'s deck.");
        return null;
    }

    public void removeAssistant(Assistant assistant) {

        this.setLastAssistantPlayed(assistant);
        deck.remove(assistant);
    }
}
