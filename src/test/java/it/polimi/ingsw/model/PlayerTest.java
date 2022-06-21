package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    @DisplayName("Testing nickname")
    void testNickname(){
        Player player1 = new Player(0);
        Player player2 = new Player(1);

        assertEquals(player1.getNickname(),"Default");
        assertNotEquals(player2.getNickname(),"xXHeavyXx");

        player1.setNickname("Frank");
        player2.setNickname("xXHeavyXx");

        assertEquals(player1.getNickname(),"Frank");
        assertEquals(player2.getNickname(),"xXHeavyXx");
    }

    @Test
    @DisplayName("Testing coins")
    void testCoins(){
        Player player1 = new Player(0);

        assertEquals(player1.getCoins(),1);

        player1.setCoins(player1.getCoins()+5);

        assertEquals(player1.getCoins(),6);
    }
    @Test
    @DisplayName("Testing initial values")
    void testInfo(){
        Player player1 = new Player(0);

        assertEquals(player1.getPlayerId(),0);
        player1.setPlayerId(2);
        assertEquals(player1.getPlayerId(),2);
        SchoolBoard schoolboard = player1.getPlayerBoard();

        Assistant assistant1 = new Assistant(4,6,0);
        Assistant assistant2 = new Assistant(5,5,1);
        Assistant assistant3 = new Assistant(2,9,2);

        ArrayList<Assistant> deck = new ArrayList<Assistant>();
        deck.add(assistant1);
        deck.add(assistant2);
        deck.add(assistant3);
        player1.setLastAssistantPlayed(assistant2);
        assertEquals(player1.getLastAssistantPlayed(),assistant2);

        player1.setDeck(deck);
        assertEquals(deck,player1.getDeck());

        player1.removeAssistant(player1.getAssistantById(1));
        assertFalse(player1.getDeck().contains(assistant2));

        assertNull(player1.getAssistantById(-1));
        player1.setTeamID(0);
        assertEquals(0, (int) player1.getTeamID());
    }

    @Test
    @DisplayName("Testing micellanea")
    void testMiscellanea(){

        GameController gameController = new GameController();
        gameController.startGame(2, "testPlayer", true);
        gameController.getCurrentGame().getPlayerByNickname("testPlayer").setActive(true);

        assertFalse(gameController.getCurrentGame().getPlayerByNickname("testPlayer").hasActiveCharacter());
        assertTrue(gameController.getCurrentGame().getPlayerByNickname("testPlayer").isActive());

    }
}
