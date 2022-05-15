package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class PlayCharacterRequest implements ClientRequest {

    Integer characterNumber;

    public PlayCharacterRequest(Integer characterNumber) {
        this.characterNumber = characterNumber;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getCharacterNumber() {
        return characterNumber;
    }
}
