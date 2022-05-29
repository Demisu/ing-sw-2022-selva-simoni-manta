package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ServerResponse;

public class GetUpdatedBoardResponse  implements ServerResponse {

    Game gameInfo;

    public GetUpdatedBoardResponse(Game gameInfo) {

        this.gameInfo = gameInfo;
    }

    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    public Game getGameInfo() {
        return gameInfo;
    }
}

