package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Class for the Assistant cards
 */
public final class Assistant implements Serializable {

    private final Integer assistantId;
    private final Integer turnPriority;
    private final Integer motherNatureMovements;

    /**
     * @param turnPriority turn priority value of the assistant
     * @param motherNatureMovements max mother nature steps value of the assistant
     * @param assistantId unique ID of the assistant
     */
    public Assistant(Integer turnPriority, Integer motherNatureMovements, Integer assistantId){
        this.turnPriority = turnPriority;
        this.motherNatureMovements = motherNatureMovements;
        this.assistantId = assistantId;
    }

    /**
     * @return assistantId
     */
    public Integer getAssistantId() {
        return assistantId;
    }

    /**
     * @return turnPriority
     */
    public Integer getTurnPriority() {
        return turnPriority;
    }

    /**
     * @return motherNatureMovements
     */
    public Integer getMotherNatureMovements() {
        return motherNatureMovements;
    }
}
