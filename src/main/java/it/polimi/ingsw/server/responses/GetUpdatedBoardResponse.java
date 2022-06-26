package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ServerResponse;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Response with updated model inside
 */
public class GetUpdatedBoardResponse implements ServerResponse {

    private Game gameInfo;
    private HashMap<Integer, HashSet<Integer>> studentsOfClouds; //<CloudID, StudentID>

    /**
     * @param gameInfo reduced model
     * @param studentsOfClouds students mapped for each cloud, to avoid information loss
     */
    public GetUpdatedBoardResponse(Game gameInfo, HashMap<Integer, HashSet<Integer>> studentsOfClouds) {

        this.gameInfo = gameInfo;
        this.studentsOfClouds = studentsOfClouds;
    }

    /**
     * @param handler handler for the response
     */
    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    /**
     * @return gameInfo
     */
    public Game getGameInfo() {
        return gameInfo;
    }
}

