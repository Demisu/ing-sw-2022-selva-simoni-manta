package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

import java.io.Serializable;

/**
 * Interface for client requests to the server
 */
public interface ClientRequest extends Serializable {

    /**
     * General handle method for the client requests
     *
     * @param handler handler to handle request
     * @return Server response for the request
     */
    ServerResponse handle(ClientRequestHandler handler);
}
