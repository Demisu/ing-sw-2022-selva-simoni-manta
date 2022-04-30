package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class IslandTest {
    @Test
    @DisplayName("Testing the number of student present")
    void testGetStudentNumber() {
        Island island1 = new Island(0);
        island1.addStudent(23);
        island1.addStudent(21);
        island1.addStudent(20);
        island1.addStudent(7);

        assertEquals(island1.getStudentNumber(Color.YELLOW),4);
        assertEquals(island1.getStudentNumber(Color.RED),0);
        assertEquals(island1.getStudentNumber()[0],4);

        island1.addStudent(30);
    }
    @Test
    @DisplayName("Testing students")
    void testIslandStudents() {
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

        HashSet<Integer> bro = new HashSet<Integer>();


        assertTrue(island.getStudents().contains(30),"Added student should exist");
        island.removeStudent(30);
        assertFalse(island.getStudents().contains(30),"Removed student should not exist");


        //Player p1 = new Player(0);
        //p1.setTowerColor(TowerColor.BLACK);
        //p1.getPlayerBoard().setProfessor(Color.YELLOW, true);
        //Player p2 = new Player(1);
        //p2.getPlayerBoard().setProfessor(Color.GREEN, true);
        //p2.setTowerColor(TowerColor.WHITE);

        //team1.add(p1);
        //team2.add(p2);
        //teams.add(team1);
        //teams.add(team2);

        //island.resolveIsland(teams);

        //assertEquals(TowerColor.BLACK, island.getTowersColor(), "Color is expected to turn BLACK due to player 1 winning");

    }
}
