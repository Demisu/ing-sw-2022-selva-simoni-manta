package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to set player number
 */
public class SetPlayerNumberRequest implements ClientRequest {

    private String nickname;
    private Integer number;
    private Boolean expertMode;

    /**
     * @param nickname requester
     * @param number players
     * @param expertMode if expert mode
     */
    public SetPlayerNumberRequest(String nickname, Integer number, Boolean expertMode) {
        this.nickname = nickname;
        this.number = number;
        this.expertMode = expertMode;
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

    /**
     * @return number
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * @return expertMode
     */
    public Boolean getExpertMode() {
        return expertMode;
    }
}
