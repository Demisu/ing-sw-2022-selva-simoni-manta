package it.polimi.ingsw.server;

import java.io.Serializable;

public interface ServerResponse extends Serializable {

    void handle(ServerResponseHandler handler);

}
