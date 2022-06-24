package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.server.responses.GetUpdatedBoardResponse;
import it.polimi.ingsw.server.responses.OperationResultResponse;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Controller used by the Server
 */
public class ServerController implements ClientRequestHandler {

    private final GameController gameController;
    private Boolean gameExists = false;
    private Integer connectedPlayers = 0;

    /**
     * @param gameController gameController
     */
    public ServerController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * @param nickname nickname of the disconnected player
     */
    public void playerDisconnected(String nickname){
        gameController.setInactive(nickname);
    }

    /**
     * Checks if the player is the current turn owner and if mother nature is allowed to be moved now
     *
     * @param req MoveMotherNatureRequest
     * @return OperationResultResponse
     */
    @Override
    public ServerResponse handle(MoveMotherNatureRequest req) {

        //Not current player
        if(!gameController.getCurrentGame().getCurrentPlayer().equals(req.getNickname())){
            return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to move mother nature.");
        }
        //Mother nature already moved in this turn
        if(gameController.getCurrentGame().getMovedMotherNatureInTurn()){
            return new OperationResultResponse(false, "Mother nature already moved in this turn");
        }
        gameController.moveMotherNature(req.getMovements());
        return new OperationResultResponse(true, "Moved mother nature by " + req.getMovements() + " steps.");
    }

    /**
     * Handle for MoveStudentRequests, checks all possible bad requests (source is an island, trying to steal students
     * from other schoolboards, etc) and in case sends back a KO result.
     * If the request is correct and valid, moves the student and send back a OK result with a confirmation message
     *
     * @param req MoveStudentRequest to handle
     * @return ServerResponse with result and message, depending on the success
     */
    @Override
    public ServerResponse handle(MoveStudentRequest req) {

        Game game = gameController.getCurrentGame();
        Player player = game.getPlayerByNickname(req.getNickname());
        //If not current player
        if(!game.getCurrentPlayer().equals(req.getNickname())){
            return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to move student.");
        }
        //If the input student is not contained in the source
        if(!game.getStudentAccessiblePieceByID(req.getSourceId()).getStudents().contains(req.getStudentId())){
            return new OperationResultResponse(false, "The selected student is not contained in the source");
        }
        //If source is an island
        if(game.getIslandByID(req.getSourceId()) != null){
            return new OperationResultResponse(false, "Cannot take a student from an island");
        }
        //If source is a schoolboard, but is not owned by the player
        if(game.getSchoolBoardByID(req.getSourceId()) != null && !player.getPlayerBoard().getPieceID().equals(req.getSourceId())){
            return new OperationResultResponse(false, "Cannot take a student from another schoolboard");
        }
        //If target is a schoolboard, but is not owned by the player
        if(game.getSchoolBoardByID(req.getTargetId()) != null && !player.getPlayerBoard().getPieceID().equals(req.getTargetId())){
            return new OperationResultResponse(false, "Cannot move a student to another schoolboard");
        }
        //If the player already moved all the students for this turn, and the student isn't moved from a cloud
        if(game.getStudentsToMove() == game.getMovedStudentsInTurn() && game.getCloudByID(req.getSourceId()) == null){
            return new OperationResultResponse(false, "Moved all students for this turn");
        }
        //If the player already chose the max number of students from a cloud
        if(game.getCloudByID(req.getSourceId()) != null && game.getMovedFromCloudInTurn() >= game.getStudentsForClouds()){
            return new OperationResultResponse(false, "Cannot choose another cloud");
        }
        //Ok
        gameController.moveStudent(req.getStudentId(), req.getSourceId(), req.getTargetId());
        //If source is not a cloud
        if(game.getCloudByID(req.getSourceId()) == null){
            //Adds 1 moved student
            game.setMovedStudentsInTurn(game.getMovedStudentsInTurn() + 1);
        } else {
            //Adds 1 student moved from a cloud
            game.setMovedFromCloudInTurn(game.getMovedFromCloudInTurn() + 1);
        }
        return new OperationResultResponse(true, "Moved student " + req.getStudentId() + " from " + req.getSourceId() + " to " + req.getTargetId());
    }

    /**
     * Checks if the current phase is planning, if the assistant was already played and if the player is current
     *
     * @param req PlayAssistantRequest
     * @return OperationResultResponse
     */
    @Override
    public ServerResponse handle(PlayAssistantRequest req) {

        //If it is not planning phase, req is invalid
        if(!gameController.getCurrentGame().getCurrentPhase().equals(GamePhase.PLANNING)){
            return new OperationResultResponse(false, "Planning phase ended or not yet started");
        }
        String nickname = req.getNickname();
        Player requestPlayer = gameController.getCurrentGame().getPlayerByNickname(nickname);
        if(!gameController.getCurrentGame().getCurrentPlayer().equals(nickname)){
            return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to play assistant.");
        }
        for (Player player : gameController.getCurrentGame().getPlayers()) {
            if(player.getLastAssistantPlayed() == null){
                continue;
            }
            if(player.getLastAssistantPlayed().getAssistantId().equals(requestPlayer.getDeck().get(req.getAssistantNumber()).getAssistantId())){
                return new OperationResultResponse(false, "Assistant already played during this turn");
            }
        }
        gameController.playAssistant(req.getNickname(), req.getAssistantNumber());
        return new OperationResultResponse(true, "Played assistant " + req.getAssistantNumber() + " of player " + req.getNickname());
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
        //If not current player
        if(!currentGame.getCurrentPlayer().equals(req.getNickname())){
            return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to play character.");
        }
        //If not action phase
        if(!currentGame.getCurrentPhase().equals(GamePhase.ACTION)){
            return new OperationResultResponse(false, "Action phase has ended or has not yet started");
        }
        //If not enough coins
        if(currentGame.getPlayerByNickname(req.getNickname()).getCoins() < characterToPlay.getCost()){
            return new OperationResultResponse(false, req.getNickname() + " doesn't have enough coins, unable to play character.");
        }
        //If already used
        if(characterToPlay.getHasBeenUsed()){
            return new OperationResultResponse(false, "Character already played during this round");
        }

        //Everything is ok, play character
        gameController.playCharacter(req);
        return new OperationResultResponse(true, "Played character " + req.getCharacterNumber());
    }

    /**
     * @param req Request needed to set the nickname. It must contain a nickname.
     * @return SetNicknameResponse with result true if the user is the game creator (the first player to join).
     *         False will be returned in case the game has already started and is waiting players
     */
    @Override
    public ServerResponse handle(SetNicknameRequest req) {

        //Create game
        if(!gameExists){
            return new SetNicknameResponse(true, true, "You are the game creator");
        }
        //If the game is full and the user is not reconnecting to an inactive account
        if(connectedPlayers >= gameController.getPlayerNumber()
                && gameController.getCurrentGame().getPlayerByNickname(req.getNickname()) == null){
            return new SetNicknameResponse(false, false, "Game is full");
        }
        //If the client is reconnecting to an active player
        if(gameController.getCurrentGame().getPlayerByNickname(req.getNickname()) != null
                && gameController.getCurrentGame().getPlayerByNickname(req.getNickname()).isActive()){
            System.out.println("Client tried to connect to active player " + req.getNickname());
            return new SetNicknameResponse(false, false, "Player is already connected");
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
        }
        Boolean success = gameController.addPlayer(req.getNickname());
        if(!success){
            System.out.println("ERROR: Error adding player (from SetPlayerNumberRequest)");
        }
        connectedPlayers++;
        return new OperationResultResponse(false, "Someone already created a game, joining...\n" +
                "The game will start as soon as all players have joined.");
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

    /**
     * Handles passing turn, checks if the player is current and if he didn-t play an assistant yet
     * (in this case sets a dummy assistant to avoid null pointer exceptions)
     *
     * @param req PassTurnRequest
     * @return OperationResultResponse
     */
    @Override
    public ServerResponse handle(PassTurnRequest req) {

        Game currentGame = gameController.getCurrentGame();
        //If not current player
        if(!currentGame.getCurrentPlayer().equals(req.getNickname())){
            return new OperationResultResponse(false, req.getNickname() + "'s turn has not yet started, unable to end it.");
        }
        //If he didn't play an assistant
        if(currentGame.getPlayerByNickname(req.getNickname()).getLastAssistantPlayed() == null){
            currentGame.getPlayerByNickname(req.getNickname()).setLastAssistantPlayed(new Assistant(11, 0, -1));
        }
        currentGame.nextPlayer();
        return new OperationResultResponse(true, req.getNickname() + "'s turn ended.\n" +
                currentGame.getCurrentPlayer() +
                "'s turn started.");
    }

    /**
     * Waits until all the players have connected
     *
     * @param req WaitingRequest
     * @return OperationResultResponse
     */
    @Override
    public ServerResponse handle(WaitingRequest req) {

        while(connectedPlayers < gameController.getPlayerNumber()){}

        System.out.println("ALL PLAYERS JOINED, GAME HAS STARTED");

        return new OperationResultResponse(true, "Ready");
    }

    /**
     * Checks if game has started
     *
     * @param req GameStartedRequest
     * @return OperationResultResponse
     */
    @Override
    public ServerResponse handle(GameStartedRequest req) {

        if(gameController.getCurrentGame() == null || connectedPlayers < gameController.getPlayerNumber()) {
            return new OperationResultResponse(false, "Waiting players");
        }

        System.out.println("ALL PLAYERS JOINED, GAME HAS STARTED");

        return new OperationResultResponse(true, "Ready");
    }

    /**
     * @return number of connected players
     */
    public Integer getConnectedPlayers() {
        return connectedPlayers;
    }

    /**
     * @param connectedPlayers number of connected players
     */
    public void setConnectedPlayers(Integer connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }

    /**
     * @return gameController reference
     */
    public GameController getGameController() {
        return gameController;
    }
}
