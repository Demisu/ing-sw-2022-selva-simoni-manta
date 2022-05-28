package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class PlayCharacterRequest implements ClientRequest {

    Integer characterNumber;
    String nickname;

    public PlayCharacterRequest(Integer characterNumber, String nickname) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getCharacterNumber() {
        return characterNumber;
    }

    public String getNickname() {
        return nickname;
    }
}
