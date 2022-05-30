package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.responses.GetUpdatedBoardResponse;
import it.polimi.ingsw.server.responses.OperationResultResponse;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

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

        if(gameController.getCurrentGame().getCurrentPlayer().equals(req.getNickname())){
            gameController.moveMotherNature(req.getMovements());
            return new OperationResultResponse(true, "Moved mother nature by " + req.getMovements() + " steps.");
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to move mother nature.");
    }

    @Override
    public ServerResponse handle(MoveStudentRequest req) {

        if(gameController.getCurrentGame().getCurrentPlayer().equals(req.getNickname())){
            gameController.moveStudent(req.getStudentId(), req.getSourceId(), req.getTargetId());
            return new OperationResultResponse(true, "Moved student " + req.getStudentId() + " from " + req.getSourceId() + " to " + req.getTargetId());
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to move student.");
    }

    @Override
    public ServerResponse handle(PlayAssistantRequest req) {

        String nickname = req.getNickname();
        Player requestPlayer = gameController.getCurrentGame().getPlayerByNickname(nickname);
        if(gameController.getCurrentGame().getCurrentPlayer().equals(nickname)){
            for (Player player : gameController.getCurrentGame().getPlayers()) {
                if(player.getLastAssistantPlayed() == null){
                    continue;
                }
                if(player.getLastAssistantPlayed().getAssistantId() == requestPlayer.getDeck().get(req.getAssistantNumber()).getAssistantId()){
                    return new OperationResultResponse(false, "Assistant already played during this turn");
                }
            }
            gameController.playAssistant(req.getNickname(), req.getAssistantNumber());
            return new OperationResultResponse(true, "Played assistant " + req.getAssistantNumber() + " of player " + req.getNickname());
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to play assistant.");
}

    @Override
    public ServerResponse handle(PlayCharacterRequest req) {

        if(gameController.getCurrentGame().getCurrentPlayer().equals(req.getNickname())){
            gameController.playCharacter(req.getNickname(), req.getCharacterNumber());
            return new OperationResultResponse(true, "Played character " + req.getCharacterNumber());
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to play character.");
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
            gameController.startGame(req.getNumber(), req.getNickname(), req.getExpertMode());
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
                    Waiting for all players...
                    """);
        }
    }

    @Override
    public ServerResponse handle(GetUpdatedBoardRequest req) {

        return new GetUpdatedBoardResponse(gameController.getCurrentGame().getReducedModel());

    }

    @Override
    public ServerResponse handle(PassTurnRequest req) {

        if(gameController.getCurrentGame().getCurrentPlayer().equals(req.getNickname())){
            gameController.nextPlayer();
            return new OperationResultResponse(true, req.getNickname() + "'s turn ended.\n" +
                                                            gameController.getCurrentGame().getCurrentPlayer() +
                                                            "'s turn started.");
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to end it.");
    }

    @Override
    public ServerResponse handle(WaitingRequest req) {

        while(connectedPlayers < gameController.getPlayerNumber()){}

        System.out.println("ALL PLAYERS JOINED, GAME HAS STARTED");

        return new OperationResultResponse(true, "Ready");
    }
}
