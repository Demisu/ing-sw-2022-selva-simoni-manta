package it.polimi.ingsw.server;

import java.io.Serializable;

public interface ServerRequest extends Serializable {

    void handle(ServerRequestHandler handler);
}
