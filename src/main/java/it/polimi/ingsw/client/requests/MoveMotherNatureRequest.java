package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class MoveMotherNatureRequest implements ClientRequest {

    final Integer movements;
    private String nickname;

    public MoveMotherNatureRequest(Integer movements, String nickname) {
        this.movements = movements;
        this.nickname = nickname;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getMovements() {
        return movements;
    }

    public String getNickname() {
        return nickname;
    }
}
