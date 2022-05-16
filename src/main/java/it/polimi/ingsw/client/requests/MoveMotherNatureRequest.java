package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class MoveMotherNatureRequest implements ClientRequest {

    public final Integer movements;

    public MoveMotherNatureRequest(Integer movements) {
        this.movements = movements;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getMovements() {
        return movements;
    }
}
