package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.ClientRequestHandler;
import it.polimi.ingsw.client.ClientResponse;
import it.polimi.ingsw.client.ClientResponseHandler;
import it.polimi.ingsw.server.ServerResponse;

public class PlayAssistantRequest implements ClientRequest {


    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }
}
