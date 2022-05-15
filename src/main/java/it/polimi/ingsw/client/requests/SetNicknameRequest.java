package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

public class SetNicknameRequest implements ClientRequest {

    String nickname;

    public SetNicknameRequest(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return (new SetNicknameResponse(nickname));
    }

    public String getNickname() {
        return nickname;
    }
}
