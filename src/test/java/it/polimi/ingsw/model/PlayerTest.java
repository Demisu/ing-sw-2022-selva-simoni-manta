package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
