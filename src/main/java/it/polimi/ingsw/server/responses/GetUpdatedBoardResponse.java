package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ServerResponse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GetUpdatedBoardResponse  implements ServerResponse {

    Game gameInfo;
    HashMap<Integer, HashSet<Integer>> studentsOfClouds; //<CloudID, StudentID>

    public GetUpdatedBoardResponse(Game gameInfo, HashMap<Integer, HashSet<Integer>> studentsOfClouds) {

        this.gameInfo = gameInfo;
        this.studentsOfClouds = studentsOfClouds;
    }

    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    public Game getGameInfo() {
        return gameInfo;
    }

    public HashMap<Integer, HashSet<Integer>> getStudentsOfClouds() {
        return studentsOfClouds;
    }
}

