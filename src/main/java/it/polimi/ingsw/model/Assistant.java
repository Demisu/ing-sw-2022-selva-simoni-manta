package it.polimi.ingsw.model;

public final class Assistant{

    private final int assistantId;
    private final int turnPriority;
    private final int motherNatureMovements;

    public Assistant(int turnPriority, int motherNatureMovements, int assistantId){
        this.turnPriority = turnPriority;
        this.motherNatureMovements = motherNatureMovements;
        this.assistantId = assistantId;
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

}
