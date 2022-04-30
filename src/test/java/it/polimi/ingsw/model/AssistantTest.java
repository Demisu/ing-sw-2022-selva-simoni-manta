package it.polimi.ingsw.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistantTest {

    @Test
    @DisplayName("General test for assistant Class")
    void assistantTest() {

        Assistant testAssistant1 = new Assistant(2,3, 1);
        Assistant testAssistant2 = new Assistant(3,4, 2);

        //assistantId
        assertEquals(1, testAssistant1.getAssistantId());
        assertEquals(2, testAssistant2.getAssistantId());

        //turnPriority
        assertEquals(2, testAssistant1.getTurnPriority());
        assertEquals(3, testAssistant2.getTurnPriority());

        //motherNatureMovements
        assertEquals(3, testAssistant1.getMotherNatureMovements());
        assertEquals(4, testAssistant2.getMotherNatureMovements());

    }
}