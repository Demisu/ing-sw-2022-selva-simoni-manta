package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.server.requests.BeginGameNotifyRequest;
import it.polimi.ingsw.server.requests.BeginPlayerRoundRequest;
import it.polimi.ingsw.server.requests.EndGameNotifyRequest;

/**
 * A method for every possible Request the Server may make to the Client
 * these methods are implemented in the server.requests package, one class each
 */

public interface ServerRequestHandler {

    ServerResponse handle(MoveMotherNatureRequest req);

    ServerResponse handle(MoveStudentRequest req);

    ServerResponse handle(PlayAssistantRequest req);

    ServerResponse handle(PlayCharacterRequest req);

    ServerResponse handle(PlayerRoundEndedRequest req);
}
