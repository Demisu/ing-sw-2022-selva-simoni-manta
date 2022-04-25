package it.polimi.ingsw.model;

public class Assistant{

    private int assistantId;
    private int turnPriority;
    private int motherNatureMovements;

    public Assistant(int turnPriority, int motherNatureMovements){
        this.turnPriority=turnPriority;
        this.motherNatureMovements=motherNatureMovements;
    }

    public int getAssistantId() {
        return assistantId;
    }

    public int getTurnPriority() {
        return turnPriority;
    }

    public int getMotherNatureMovements() {
        return motherNatureMovements;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public void setTurnPriority(int turnPriority) {
        this.turnPriority = turnPriority;
    }

    public void setMotherNatureMovements(int motherNatureMovements) {
        this.motherNatureMovements = motherNatureMovements;
    }
}
