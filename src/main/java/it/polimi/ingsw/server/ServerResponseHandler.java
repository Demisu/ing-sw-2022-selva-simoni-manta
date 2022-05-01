package it.polimi.ingsw.server;

import it.polimi.ingsw.server.responses.*;

public interface ServerResponseHandler {

    void handle(AssistantPlayedResponse res);

    void handle(CharacterPlayedResponse res);

    void handle(MotherNatureMovedResponse res);

    void handle(StudentMovedResponse res);
}
