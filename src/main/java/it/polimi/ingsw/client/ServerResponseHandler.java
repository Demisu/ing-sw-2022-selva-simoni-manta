package it.polimi.ingsw.client;

import it.polimi.ingsw.server.responses.*;

/**
 * Class for the server responses handler
 */
public interface ServerResponseHandler {

    /**
     * @param res SetNicknameResponse
     * @return status of the response
     */
    Integer handle(SetNicknameResponse res);

    /**
     * @param res OperationResultResponse
     * @return status of the response
     */
    Boolean handle(OperationResultResponse res);

    /**
     * @param res GetUpdatedBoardResponse
     */
    void handle(GetUpdatedBoardResponse res);
}
