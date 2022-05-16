package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class SetPlayerNumberRequest implements ClientRequest {

    String nickname;
    Integer number;

    public SetPlayerNumberRequest(String nickname, Integer number) {
        this.nickname = nickname;
        this.number = number;
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
}
