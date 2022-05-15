package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ServerResponseHandler;

import java.io.Serializable;

public interface ServerResponse extends Serializable {

    void handle(ServerResponseHandler handler);

}
