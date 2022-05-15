package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.client.ServerResponseHandler;

public class SetNicknameResponse implements ServerResponse {

    String nickname;
    public SetNicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handle(ServerResponseHandler handler) {

    }

    public String getNickname() {
        return nickname;
    }
}
