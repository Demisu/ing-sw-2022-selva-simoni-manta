package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.ClientResponseHandler;

public class MoveMotherNatureRequest implements ClientRequest {

    public final Integer movements;

    public MoveMotherNatureRequest(Integer movements) {
        this.movements = movements;
    }

    @Override
    public void handle(ClientResponseHandler handler) {

    }
}
