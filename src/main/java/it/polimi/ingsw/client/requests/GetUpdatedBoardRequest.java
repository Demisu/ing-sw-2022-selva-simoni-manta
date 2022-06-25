package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to ask for and updated model
 */
public class GetUpdatedBoardRequest implements ClientRequest {

    private String nickname;

    /**
     * @param nickname nickname of the requester
     */
    public GetUpdatedBoardRequest(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @param handler handler to handle request
     * @return ServerResponse
     */
    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    /**
     * @return nickname of the request
     */
    public String getNickname() {
        return nickname;
    }
}