package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to pass turn
 */
public class PassTurnRequest implements ClientRequest {

    private String nickname;

    /**
     * @param nickname requester
     */
    public PassTurnRequest(String nickname) {
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
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }
}