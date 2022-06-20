package it.polimi.ingsw.model;

import static it.polimi.ingsw.model.TowerColor.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


class TeamTest {
    @Test
    @DisplayName("Testing Getters")
    void testGetters() {
        Team team1 = new Team(BLACK,0,0);

        assertEquals(team1.getTeamId(),0);
        assertEquals(team1.getTowerColor(),BLACK);
        assertEquals(team1.getTowerNumber(),0);


        Team team2 = new Team();

        assertEquals(team2.getTeamId(),-1);
    }

    @Test
    @DisplayName("Testing Setters")
    void testSetters() {
        Team team1 = new Team(BLACK, 0, 0);

        Player player1 = new Player(0);
        Player player2 = new Player(2);
        ArrayList<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);

        team1.setTowerColor(WHITE);
        assertEquals(team1.getTowerColor(), WHITE);

        team1.setTowerNumber(1);
        assertEquals(team1.getTowerNumber(), 1);

        assertNotEquals(team1.getPlayers(), players);
        team1.addPlayer(player1);
        team1.addPlayer(player2);
        assertEquals(team1.getPlayers(), players);

        team1.setWinner(true);
    }
}
