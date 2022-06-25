package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to set nickname
 */
public class SetNicknameRequest implements ClientRequest {

    private String nickname;

    /**
     * @param nickname nickname
     */
    public SetNicknameRequest(String nickname) {
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
