package it.polimi.ingsw.client;

import it.polimi.ingsw.server.responses.*;

public interface ServerResponseHandler {

    void handle(AssistantPlayedResponse res);

    void handle(CharacterPlayedResponse res);

    void handle(MotherNatureMovedResponse res);

    void handle(StudentMovedResponse res);

    Boolean handle(SetNicknameResponse res);

    Boolean handle(OperationResultResponse res);

    void handle(GetUpdatedBoardResponse res);
}
