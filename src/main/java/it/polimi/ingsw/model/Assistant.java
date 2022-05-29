package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Class for the Assistant cards
 */
public final class Assistant implements Serializable {

    private final int assistantId;
    private final int turnPriority;
    private final int motherNatureMovements;

    /**
     * @param turnPriority turn priority value of the assistant
     * @param motherNatureMovements max mother nature steps value of the assistant
     * @param assistantId unique ID of the assistant
     */
    public Assistant(int turnPriority, int motherNatureMovements, int assistantId){
        this.turnPriority = turnPriority;
        this.motherNatureMovements = motherNatureMovements;
        this.assistantId = assistantId;
    }

    /**
     * @return assistantId
     */
    public int getAssistantId() {
        return assistantId;
    }

    /**
     * @return turnPriority
     */
    public int getTurnPriority() {
        return turnPriority;
    }

    /**
     * @return motherNatureMovements
     */
    public int getMotherNatureMovements() {
        return motherNatureMovements;
    }
}
