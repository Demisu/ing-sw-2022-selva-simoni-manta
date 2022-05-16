package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.responses.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller used by the Server
 */
public class ServerController implements ClientRequestHandler {

    private ArrayList<ClientHandler> clientHandlerList;
    private GameController gameController;
    private static Boolean gameExists = false;
    private static Integer connectedPlayers = 0;
    private static Boolean gameStarted = false;

    public ServerController(GameController gameController) {
        this.gameController = gameController;
        this.clientHandlerList = new ArrayList<>();
    }

    public void addClientHandler(ClientHandler clientHandler){
        this.clientHandlerList.add(clientHandler);
    }

    //return new constructors to be updated
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
        gameController.playAssistant();
        return new OperationResultResponse(true, "Played assistant");
    }

    @Override
    public ServerResponse handle(PlayCharacterRequest req) {
        return new OperationResultResponse(true, "Played character");
    }

    @Override
    public ServerResponse handle(PlayerRoundEndedRequest req) {
        return new OperationResultResponse(true, "Round ended successfully");
    }

    @Override
    public ServerResponse handle(SetNicknameRequest req) {

        Boolean success = false;

        if(!gameExists){
            return new SetNicknameResponse(true);
        }else{
            success = gameController.addPlayer(req.getNickname());
            connectedPlayers++;
            return new SetNicknameResponse(false);
        }
    }

    @Override
    public ServerResponse handle(WaitingRequest req) {

        return new OperationResultResponse(true, "Ready");
    }

    @Override
    public ServerResponse handle(SetPlayerNumberRequest req) {

        Boolean success = false;

        if(!gameExists) {
            gameController.startGame(req.getNumber(), req.getNickname());
            gameExists = true;
            connectedPlayers++;
            return new OperationResultResponse(true, "Game created successfully");
        } else {
            success = gameController.addPlayer(req.getNickname());
            connectedPlayers++;
            return new OperationResultResponse(false, "Game already started");
        }
    }


    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the server.responses package, one Class for each
     * @param req the response received by the Server from the Client
     */


}
