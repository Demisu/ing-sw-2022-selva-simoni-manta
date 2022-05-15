package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class PlayCharacterRequest implements ClientRequest {


    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }
}
