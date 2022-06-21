package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ServerResponseHandler;

import java.io.Serializable;

/**
 * Interface for Server Responses
 */
public interface ServerResponse extends Serializable {

    /**
     * @param handler handler for the response
     */
    void handle(ServerResponseHandler handler);
}
