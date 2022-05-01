package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.ClientResponse;
import it.polimi.ingsw.client.ClientResponseHandler;

/**
 * Notifies the Server the round has ended. Server must check for TRUE
 */
public class PlayerRoundEndedRequest implements ClientRequest {

    private final Boolean status;

    public PlayerRoundEndedRequest(Boolean status) {
        this.status = status;
    }

    @Override
    public ClientResponse handle(ClientResponseHandler handler) {

        return null;
    }
}
