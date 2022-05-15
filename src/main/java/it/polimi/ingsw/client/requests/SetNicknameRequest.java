package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class SetNicknameRequest implements ClientRequest {

    String nickname;

    public SetNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
        //return (new SetNicknameResponse(nickname));
    }

    public String getNickname() {
        return nickname;
    }
}
