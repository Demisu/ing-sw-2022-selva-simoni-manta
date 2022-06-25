package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;

/**
 * A method for every possible Request the Client may make to the Server
 * these methods are implemented in the client.requests package, one class each
 */
public interface ClientRequestHandler {

    /**
     * @param moveMotherNatureRequest moveMotherNatureRequest
     * @return ServerResponse
     */
    ServerResponse handle(MoveMotherNatureRequest moveMotherNatureRequest);

    /**
     * @param moveStudentRequest moveStudentRequest
     * @return ServerResponse
     */
    ServerResponse handle(MoveStudentRequest moveStudentRequest);

    /**
     * @param playAssistantRequest playAssistantRequest
     * @return ServerResponse
     */
    ServerResponse handle(PlayAssistantRequest playAssistantRequest);

    /**
     * @param playCharacterRequest playCharacterRequest
     * @return ServerResponse
     */
    ServerResponse handle(PlayCharacterRequest playCharacterRequest);

    /**
     * @param setNicknameRequest setNicknameRequest
     * @return ServerResponse
     */
    ServerResponse handle(SetNicknameRequest setNicknameRequest);

    /**
     * @param waitingRequest waitingRequest
     * @return ServerResponse
     */
    ServerResponse handle(WaitingRequest waitingRequest);

    /**
     * @param setPlayerNumberRequest setPlayerNumberRequest
     * @return ServerResponse
     */
    ServerResponse handle(SetPlayerNumberRequest setPlayerNumberRequest);

    /**
     * @param getUpdatedBoardRequest getUpdatedBoardRequest
     * @return ServerResponse
     */
    ServerResponse handle(GetUpdatedBoardRequest getUpdatedBoardRequest);

    /**
     * @param passTurnRequest passTurnRequest
     * @return ServerResponse
     */
    ServerResponse handle(PassTurnRequest passTurnRequest);

    /**
     * @param gameStartedRequest gameStartedRequest
     * @return ServerResponse
     */
    ServerResponse handle(GameStartedRequest gameStartedRequest);
}
