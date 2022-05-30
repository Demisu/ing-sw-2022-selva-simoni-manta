package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class SetPlayerNumberRequest implements ClientRequest {

    String nickname;
    Integer number;
    Boolean expertMode;

    public SetPlayerNumberRequest(String nickname, Integer number, Boolean expertMode) {
        this.nickname = nickname;
        this.number = number;
        this.expertMode = expertMode;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getNumber() {
        return number;
    }

    public Boolean getExpertMode() {
        return expertMode;
    }
}
