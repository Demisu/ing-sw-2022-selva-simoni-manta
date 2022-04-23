package it.polimi.ingsw.model;

import java.util.HashSet;

public class Player{
    final Integer initialCoins = 1;

    private Integer playerid;
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
    
    private HashSet<Assistant> deck;
    SchoolBoard playerBoard; //package-private

    public Player(Integer playerid){
        this.playerid = playerid;
        this.coins = initialCoins;
        this.nickname = "Default";
    //    this.towerColor = towerColor;
    //    this.towerNumber = towerNumber;
        this.deck = new HashSet<>();
        this.playerBoard = new SchoolBoard();
    }

    public SchoolBoard getPlayerBoard(){
        return this.playerBoard;
    }

    public void setNickname(String nicknameOfCreator) {
        this.nickname = nicknameOfCreator;
    }
}
