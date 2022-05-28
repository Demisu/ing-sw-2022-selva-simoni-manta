package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTest {

    @Test
    @DisplayName("General test for Character Class")
    void actionsTest() {
        HashSet<Integer> students = new HashSet<>();
        Character characterTest = new Character(2,"path_to_image",false, students, 0);
        characterTest.effect();
        characterTest.move();
        characterTest.add();
        GameController controller = new GameController();
        characterTest.setGameController(controller);
    }
}