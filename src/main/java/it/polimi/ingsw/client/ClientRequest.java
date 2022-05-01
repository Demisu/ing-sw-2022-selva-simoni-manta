package it.polimi.ingsw.client;

import java.io.Serializable;

public interface ClientRequest extends Serializable {

    ClientResponse handle(ClientResponseHandler handler);

}
