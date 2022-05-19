package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.server.ServerResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GetUpdatedBoardResponse  implements ServerResponse {

    Player playerInfo;
    ArrayList<Character> characters;
    List<Island> islands;
    Set<Cloud> clouds;
    ArrayList<SchoolBoard> schoolBoards;

    public GetUpdatedBoardResponse(Player playerInfo, ArrayList<Character> characters, List<Island> islands, Set<Cloud> clouds, ArrayList<SchoolBoard> schoolBoards) {
        this.playerInfo = playerInfo;
        this.characters = characters;
        this.islands = islands;
        this.clouds = clouds;
        this.schoolBoards = schoolBoards;
    }

    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    public Player getPlayerInfo() {
        return this.playerInfo;
    }

    public ArrayList<Character> getCharacters() {
        return this.characters;
    }

    public List<Island> getIslands() {
        return this.islands;
    }

    public Set<Cloud> getClouds() {
        return this.clouds;
    }

    public ArrayList<SchoolBoard> getSchoolBoards() {
        return this.schoolBoards;
    }
}

