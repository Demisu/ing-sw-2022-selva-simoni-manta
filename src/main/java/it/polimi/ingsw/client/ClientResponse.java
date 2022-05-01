package it.polimi.ingsw.client;

import java.io.Serializable;

public interface ClientResponse extends Serializable {

    void handle(ClientResponseHandler handler);

}
