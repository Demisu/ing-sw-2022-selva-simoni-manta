package it.polimi.ingsw.client;

public interface ClientView /*will have to implement listeners*/ {

    void setClientController(ClientController clientController);

    void waitGameStartPhase();

    void setupPhase();

    void planningPhase();

    void actionPhase();

    //@Override methods that will be defined in Listener Interface
}
