package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class GetUpdatedBoardRequest implements ClientRequest {

    String nickname;
    Boolean firstRequest;

    public GetUpdatedBoardRequest(String nickname, Boolean firstRequest) {
        this.nickname = nickname;
        this.firstRequest = firstRequest;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Boolean getFirstRequest() {
        return firstRequest;
    }

    public String getNickname() {
        return nickname;
    }
}