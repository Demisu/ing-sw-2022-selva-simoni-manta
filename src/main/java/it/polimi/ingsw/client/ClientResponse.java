package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ClientResponseHandler;

import java.io.Serializable;

public interface ClientResponse extends Serializable {

    void handle(ClientResponseHandler handler);

}
