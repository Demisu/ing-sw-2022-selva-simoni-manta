package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to move mother nature
 */
public class MoveMotherNatureRequest implements ClientRequest {

    private final Integer movements;
    private final String nickname;

    /**
     * @param movements steps
     * @param nickname requester
     */
    public MoveMotherNatureRequest(Integer movements, String nickname) {
        this.movements = movements;
        this.nickname = nickname;
    }

    /**
     * @param handler handler to handle request
     * @return ServerResponse
     */
    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    /**
     * @return movements
     */
    public Integer getMovements() {
        return movements;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }
}
