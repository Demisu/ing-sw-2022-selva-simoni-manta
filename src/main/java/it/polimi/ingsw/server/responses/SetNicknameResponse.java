package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.client.ServerResponseHandler;

public class SetNicknameResponse implements ServerResponse {

    Boolean needPlayerNumber;
    Boolean success;
    String message;

    public SetNicknameResponse(Boolean needPlayerNumber, Boolean success, String message) {
        this.needPlayerNumber = needPlayerNumber;
        this.message = message;
        this.success = success;
    }

    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    public Boolean getNeedPlayerNumber() {
        return needPlayerNumber;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
