package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

import java.io.Serializable;

public interface ClientRequest extends Serializable {

    ServerResponse handle(ClientRequestHandler handler);

}
