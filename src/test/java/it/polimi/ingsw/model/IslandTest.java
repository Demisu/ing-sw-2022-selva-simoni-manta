package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {

    @Test
    @DisplayName("Testing how influence gets evaluated")
    void testResolveIsland() {
        Island island = new Island(1);
        island.setTowersColor(TowerColor.GREY);
        island.setTowersNumber(2);
        island.addStudent(1);
        island.addStudent(2);
        island.addStudent(3);
        island.addStudent(30);
        island.addStudent(31);
        ArrayList<ArrayList<Player>> teams = new ArrayList<>();
        ArrayList<Player> team1 = new ArrayList<>();
        ArrayList<Player> team2 = new ArrayList<>();

        Player p1 = new Player(0);
        p1.setTowerColor(TowerColor.BLACK);
        p1.playerBoard.setProfessorOfColor(Color.YELLOW, true);
        Player p2 = new Player(1);
        p2.playerBoard.setProfessorOfColor(Color.GREEN, true);
        p2.setTowerColor(TowerColor.WHITE);

        team1.add(p1);
        team2.add(p2);
        teams.add(team1);
        teams.add(team2);

        island.resolveIsland(teams);

        assertEquals(TowerColor.BLACK, island.getTowersColor(), "Color is expected to turn BLACK due to player 1 winning");

    }
}
