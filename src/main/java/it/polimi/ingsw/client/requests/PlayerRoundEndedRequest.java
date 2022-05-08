package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.ClientRequestHandler;
import it.polimi.ingsw.client.ClientResponse;
import it.polimi.ingsw.client.ClientResponseHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Notifies the Server the round has ended. Server must check for TRUE
 */
public class PlayerRoundEndedRequest implements ClientRequest {

    private final Boolean status;

    public PlayerRoundEndedRequest(Boolean status) {
        this.status = status;
    }


    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }
}
