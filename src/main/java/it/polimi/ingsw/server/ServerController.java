package it.polimi.ingsw.server;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.responses.*;

/**
 * Controller used by the Server
 */
public class ServerController implements ClientRequestHandler {

    private final ClientHandler clientHandler;

    private GameController gameController;

    private static Boolean gameExists = false;

    public ServerController(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
        gameController = new GameController();
    }

    //return new constructors to be updated
    @Override
    public ServerResponse handle(MoveMotherNatureRequest req) {
        return new MotherNatureMovedResponse();
    }

    @Override
    public ServerResponse handle(MoveStudentRequest req) {
        return new StudentMovedResponse();
    }

    @Override
    public ServerResponse handle(PlayAssistantRequest req) {
        return new AssistantPlayedResponse();
    }

    @Override
    public ServerResponse handle(PlayCharacterRequest req) {
        return new CharacterPlayedResponse();
    }

    @Override
    public ServerResponse handle(PlayerRoundEndedRequest req) {
        return new PlayerRoundEndedResponse();
    }

    @Override
    public ServerResponse handle(SetNicknameRequest req) {
        if(!gameExists){
            gameExists = true;
            return new SetNicknameResponse(true);
        }else{
            gameController.getCurrentGame();
            return new SetNicknameResponse(false);
        }
    }

    @Override
    public ServerResponse handle(WaitingRequest req) {
        return null; //TO BE REVISED.
    }


    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the server.responses package, one Class for each
     * @param req the response received by the Server from the Client
     */


}
