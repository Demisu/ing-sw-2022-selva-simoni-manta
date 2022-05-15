package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.server.ServerResponse;

/**
 * A method for every possible Request the Client may make to the Server
 * these methods are implemented in the client.requests package, one class each
 */

public interface ClientRequestHandler {

    ServerResponse handle(MoveMotherNatureRequest req);

    ServerResponse handle(MoveStudentRequest req);

    ServerResponse handle(PlayAssistantRequest req);

    ServerResponse handle(PlayCharacterRequest req);

    ServerResponse handle(PlayerRoundEndedRequest req);

    ServerResponse handle(SetNicknameRequest req);

    ServerResponse handle(WaitingRequest waitingRequest);
}
