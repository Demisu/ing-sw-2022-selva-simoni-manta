package it.polimi.ingsw.client;

import it.polimi.ingsw.server.responses.*;

public interface ServerResponseHandler {

    Integer handle(SetNicknameResponse res);

    Boolean handle(OperationResultResponse res);

    void handle(GetUpdatedBoardResponse res);
}
