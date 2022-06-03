package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.responses.GetUpdatedBoardResponse;
import it.polimi.ingsw.server.responses.OperationResultResponse;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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

        Game game = gameController.getCurrentGame();
        Player player = game.getPlayerByNickname(req.getNickname());
        if(game.getCurrentPlayer().equals(req.getNickname())){
            //If the input student is not contained in the source
            if(!game.getStudentAccessiblePieceByID(req.getSourceId()).getStudents().contains(req.getStudentId())){
                return new OperationResultResponse(false, "The selected student is not contained in the source");
            }
            //If source is an island
            if(game.getIslandByID(req.getSourceId()) != null){
                return new OperationResultResponse(false, "Cannot take a student from an island");
            }
            //If source is a schoolboard, but is not owned by the player
            if(game.getSchoolBoardByID(req.getSourceId()) != null && !player.getPlayerBoard().equals(game.getSchoolBoardByID(req.getSourceId()))){
                return new OperationResultResponse(false, "Cannot take a student from another schoolboard");
            }
            //If target is a schoolboard, but is not owned by the player
            if(game.getSchoolBoardByID(req.getTargetId()) != null && !player.getPlayerBoard().equals(game.getSchoolBoardByID(req.getSourceId()))){
                return new OperationResultResponse(false, "Cannot move a student to another schoolboard");
            }
            //Ok
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

    /**
     * @param req Full request needed to activate character. Must include at least all the necessary info to activate
     *            the effect
     * @return OperationResultResponse with result true if the character was played correctly.
     *         False will be returned in these cases:
     *         - The player doesn't have enough coins to use it
     *         - The character has already been used during this action phase
     *         - The player's round has not yet startet
     */
    @Override
    public ServerResponse handle(PlayCharacterRequest req) {

        Game currentGame = gameController.getCurrentGame();
        Character characterToPlay = gameController.getCurrentGame().getAllCharacters()[req.getCharacterNumber()];
        if(currentGame.getCurrentPlayer().equals(req.getNickname())){

            if(currentGame.getPlayerByNickname(req.getNickname()).getCoins() < characterToPlay.getCost()){
                return new OperationResultResponse(false, req.getNickname() + " doesn't have enough coins, unable to play character.");
            }

            if(characterToPlay.getHasBeenUsed()){
                return new OperationResultResponse(false, "Character already played during this round");
            }

            //Everything is ok, play character
            gameController.playCharacter(req);
            return new OperationResultResponse(true, "Played character " + req.getCharacterNumber());
        }
        return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to play character.");
    }

    /**
     * @param req Request needed to set the nickname. It must contain a nickname.
     * @return SetNicknameResponse with result true if the user is the game creator (the first player to join).
     *         False will be returned in case the game has already started and is waiting players
     */
    @Override
    public ServerResponse handle(SetNicknameRequest req) {

        if(!gameExists){
            return new SetNicknameResponse(true, true, "You are the game creator");
        }else{
            //If the game is full and the user is not reconnecting to an inactive account
            if(connectedPlayers >= gameController.getPlayerNumber()
                    && gameController.getCurrentGame().getPlayerByNickname(req.getNickname()) == null){
                return new SetNicknameResponse(false, false, "Game is full");
            }
            Boolean success = gameController.addPlayer(req.getNickname());
            if(!success){
                System.out.println("ERROR: Error adding player (from SetNicknameRequest)");
                return new SetNicknameResponse(false, false, "Error encountered while joining the game");
            }
            connectedPlayers++;
            return new SetNicknameResponse(false, true, """
                    Connected to game lobby.
                    The game will start as soon as all players have joined.
                    Waiting for all players...""");
        }
    }

    /**
     * @param req Request needed to set the player number. It must contain the number of players, the nickname of the
     *            game owner and a boolean flag for expert mode.
     * @return OperationResultResponse with result true if the game is created successfully.
     *         False in case the game has already started during this phase (in this case, the requesting user is set
     *         as a normal player and is not the owner)
     */
    @Override
    public ServerResponse handle(SetPlayerNumberRequest req) {

        if(!gameExists) {
            gameController.startGame(req.getNumber(), req.getNickname(), req.getExpertMode());
            gameExists = true;
            connectedPlayers++;
            Game currentGame = gameController.getCurrentGame();
            System.out.println("GAME CREATED\nParameters:\nPlayer Number: " + currentGame.getPlayers().size()
                    + "\nCreator: " + currentGame.getPlayers().get(0).getNickname()
                    + "\nExpert Mode: " + currentGame.isExpertMode());
            return new OperationResultResponse(true, "Game created");
        } else {
            Boolean success = gameController.addPlayer(req.getNickname());
            if(!success){
                System.out.println("ERROR: Error adding player (from SetPlayerNumberRequest)");
            }
            connectedPlayers++;
            return new OperationResultResponse(false, "Someone already created a game, joining...\n" +
                    "The game will start as soon as all players have joined.");
        }
    }

    /**
     * @param req Request needed to get a reduced and updated game model. It must contain the nickname of the requesting
     *            player
     * @return GetUpdatedBoardResponse containing a reduced version of the game, in order to correctly visualize the
     *         game status in the client view (CLI or GUI)
     */
    @Override
    public ServerResponse handle(GetUpdatedBoardRequest req) {

        HashMap<Integer, HashSet<Integer>> studentsOfClouds = new HashMap<>();
        for (Cloud cloud : gameController.getCurrentGame().getClouds()){
            studentsOfClouds.put(cloud.getPieceID(), new HashSet<>());
            cloud.getStudents().forEach(student -> studentsOfClouds.get(cloud.getPieceID()).add(student));
        }
        return new GetUpdatedBoardResponse(gameController.getCurrentGame().getReducedModel(), studentsOfClouds);
    }

    @Override
    public ServerResponse handle(PassTurnRequest req) {

        Game currentGame = gameController.getCurrentGame();
        if(currentGame.getCurrentPlayer().equals(req.getNickname())){
            currentGame.nextPlayer();
            return new OperationResultResponse(true, req.getNickname() + "'s turn ended.\n" +
                                                            currentGame.getCurrentPlayer() +
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
