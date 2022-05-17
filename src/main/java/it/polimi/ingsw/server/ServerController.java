package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.server.responses.*;

import java.util.ArrayList;

/**
 * Controller used by the Server
 */
public class ServerController implements ClientRequestHandler {

    private final ArrayList<ClientHandler> clientHandlerList;
    private final GameController gameController;
    private static Boolean gameExists = false;
    private static Integer connectedPlayers = 0;

    public ServerController(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerList = new ArrayList<>();
    }

    public void addClientHandler(ClientHandler clientHandler){
        this.clientHandlerList.add(clientHandler);
    }

    //return new constructors to be updated

    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the server. Responses package, one Class for each
     * @param req the response received by the Server from the Client
     */

    @Override
    public ServerResponse handle(MoveMotherNatureRequest req) {
        gameController.moveMotherNature(req.getMovements());
        return new OperationResultResponse(true, "Moved mother nature by " + req.getMovements() + " steps.");
    }

    @Override
    public ServerResponse handle(MoveStudentRequest req) {
        gameController.moveStudent(req.getStudentId(), req.getSourceId(), req.getTargetId());
        return new OperationResultResponse(true, "Moved student " + req.getStudentId() + " from " + req.getSourceId() + " to " + req.getTargetId());
    }

    @Override
    public ServerResponse handle(PlayAssistantRequest req) {
        gameController.playAssistant(req.getNickname(), req.getAssistantNumber());
        return new OperationResultResponse(true, "Played assistant " + req.getAssistantNumber() + " of player " + req.getNickname());
    }

    @Override
    public ServerResponse handle(PlayCharacterRequest req) {
        return new OperationResultResponse(true, "Played character " + req.getCharacterNumber());
    }

    @Override
    public ServerResponse handle(PlayerRoundEndedRequest req) {
        return new OperationResultResponse(true, "Round ended successfully");
    }

    @Override
    public ServerResponse handle(SetNicknameRequest req) {

        if(!gameExists){
            return new SetNicknameResponse(true, "You are the game creator");
        }else{
            Boolean success = gameController.addPlayer(req.getNickname());
            if(!success){
                System.out.println("ERROR: Error adding player (from SetNicknameRequest)");
            }
            connectedPlayers++;
            return new SetNicknameResponse(false, """
                    Connected to game lobby.
                    The game will start as soon as all players have joined.
                    Waiting for all players...""");
        }
    }

    @Override
    public ServerResponse handle(SetPlayerNumberRequest req) {

        if(!gameExists) {
            gameController.startGame(req.getNumber(), req.getNickname());
            gameExists = true;
            connectedPlayers++;
            return new OperationResultResponse(true, "Game created");
        } else {
            Boolean success = gameController.addPlayer(req.getNickname());
            if(!success){
                System.out.println("ERROR: Error adding player (from SetPlayerNumberRequest)");
            }
            connectedPlayers++;
            return new OperationResultResponse(false, """
                    Game already playing, added to lobby.
                    The game will start as soon as all players have joined.
                    Waiting for all players...""");
        }
    }

    @Override
    public ServerResponse handle(WaitingRequest req) {

        return new OperationResultResponse(true, "Ready");
    }
}
