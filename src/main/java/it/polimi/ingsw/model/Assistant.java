package it.polimi.ingsw.model;

public class Assistant{
    private AssistantName name;/*va bene così?*/
    private int turnPriority;
    private int motherNatureMovements;

    public Assistant(int turnPriority, int motherNatureMovements){
        this.turnPriority=turnPriority;
        this.motherNatureMovements=motherNatureMovements;
    }
    
}
