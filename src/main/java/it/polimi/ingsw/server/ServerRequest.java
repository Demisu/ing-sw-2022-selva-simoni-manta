package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientResponse;

import java.io.Serializable;

public interface ServerRequest extends Serializable {

    ClientResponse handle(ServerRequestHandler handler);
}
