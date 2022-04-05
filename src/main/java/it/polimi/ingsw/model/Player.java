package it.polimi.ingsw.model;

import java.util.HashSet;

public class Player{
    final Integer initialCoins = 1;

    private Integer playerid;
    private Integer coins;
    private String nickname;
    private TowerColor towerColor;
    private Integer towerNumber;//Towers in player's schoolboard (not island)
    //Check that the number doesn't go below zero. 
    
    private HashSet<Assistant> deck;
    private SchoolBoard playerBoard;

    public Player(Integer playerid){
        this.playerid = playerid;
        this.coins = initialCoins;
    }

    public void setNickname(String nicknameOfCreator) {
        this.nickname = nicknameOfCreator
    }
}
