package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static it.polimi.ingsw.model.Color.*;
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

        assertEquals(island1.getStudentNumber(YELLOW),4);
        assertEquals(island1.getStudentNumber(Color.RED),0);
        assertEquals(island1.getStudentNumber()[0],4);

        island1.addStudent(30);
    }
    @Test
    @DisplayName("Testing TowerColor")
    void testGetTowerColor() {
        Island island1 = new Island(0);
        island1.setTowersColor(TowerColor.GREY);
        assertEquals(island1.getTowersColor(),TowerColor.GREY);
    }
    @Test
    @DisplayName("Testing ResolveIsland")
    void testResolveIsland() {
        Island island1 = new Island(0);
        island1.setTowersColor(TowerColor.WHITE);
        ArrayList<Team> teams = new ArrayList<Team>();
        Team team1 = new Team(TowerColor.BLACK,1,0);
        Team team2 = new Team(TowerColor.WHITE,0,0);
        Player player1 = new Player(0);
        Player player2 = new Player(1);
        Player player3 = new Player(2);
        Player player4 = new Player(3);
        player1.getPlayerBoard().setProfessor(YELLOW,true);
        player1.getPlayerBoard().setProfessor(BLUE,false);
        player1.getPlayerBoard().setProfessor(GREEN,false);
        player1.getPlayerBoard().setProfessor(RED,false);
        player1.getPlayerBoard().setProfessor(PURPLE,false);
        player2.getPlayerBoard().setProfessor(YELLOW,false);
        player2.getPlayerBoard().setProfessor(BLUE,true);
        player2.getPlayerBoard().setProfessor(GREEN,false);
        player2.getPlayerBoard().setProfessor(RED,false);
        player2.getPlayerBoard().setProfessor(PURPLE,false);
        player3.getPlayerBoard().setProfessor(YELLOW,false);
        player3.getPlayerBoard().setProfessor(BLUE,false);
        player3.getPlayerBoard().setProfessor(GREEN,true);
        player3.getPlayerBoard().setProfessor(RED,false);
        player3.getPlayerBoard().setProfessor(PURPLE,false);
        player4.getPlayerBoard().setProfessor(YELLOW,false);
        player4.getPlayerBoard().setProfessor(BLUE,false);
        player4.getPlayerBoard().setProfessor(GREEN,false);
        player4.getPlayerBoard().setProfessor(RED,false);
        player4.getPlayerBoard().setProfessor(PURPLE,false);
        team1.addPlayer(player1);
        team1.addPlayer(player2);
        team2.addPlayer(player3);
        team2.addPlayer(player4);
        teams.add(team1);
        teams.add(team2);
        island1.resolveIsland(teams);
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
