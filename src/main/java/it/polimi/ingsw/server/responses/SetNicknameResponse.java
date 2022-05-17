package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.client.ServerResponseHandler;

public class SetNicknameResponse implements ServerResponse {

    String nickname;
    Boolean needPlayerNumber;
    String message;

    public SetNicknameResponse(Boolean needPlayerNumber, String message) {
        this.needPlayerNumber = needPlayerNumber;
        this.message = message;
    }

    public SetNicknameResponse(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    public String getNickname() {
        return nickname;
    }

    public Boolean getNeedPlayerNumber() {
        return needPlayerNumber;
    }

    public String getMessage() {
        return message;
    }
}
