package it.polimi.ingsw.client;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.server.requests.BeginGameNotifyRequest;
import it.polimi.ingsw.server.requests.BeginPlayerRoundRequest;
import it.polimi.ingsw.server.requests.EndGameNotifyRequest;

/**
 * A method for every possible Request the Server may make to the Client
 * these methods are implemented in the server.requests package, one class each
 */

public interface ServerRequestHandler {

    ClientResponse handle(MoveMotherNatureRequest req);

    ClientResponse handle(MoveStudentRequest req);

    ClientResponse handle(PlayAssistantRequest req);

    ClientResponse handle(PlayCharacterRequest req);

    ClientResponse handle(PlayerRoundEndedRequest req);
}
