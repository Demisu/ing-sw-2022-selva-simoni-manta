package it.polimi.ingsw.server;

import it.polimi.ingsw.server.requests.BeginGameNotifyRequest;
import it.polimi.ingsw.server.requests.BeginPlayerRoundRequest;
import it.polimi.ingsw.server.requests.EndGameNotifyRequest;

/**
 * A method for every possible Request the Client may make to the Server
 */

public interface ServerRequestHandler {

    ServerResponse handle(BeginGameNotifyRequest req);

    ServerResponse handle(BeginPlayerRoundRequest req);

    ServerResponse handle(EndGameNotifyRequest req);
}
