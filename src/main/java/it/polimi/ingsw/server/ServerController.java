package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientRequestHandler;
import it.polimi.ingsw.client.ClientResponse;
import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.server.requests.BeginGameNotifyRequest;
import it.polimi.ingsw.server.requests.BeginPlayerRoundRequest;
import it.polimi.ingsw.server.requests.EndGameNotifyRequest;
import it.polimi.ingsw.server.responses.*;

/**
 * Controller used by the Server
 */
public class ServerController implements ClientRequestHandler {

    private final ClientHandler clientHandler;

    public ServerController(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
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
        return new SetNicknameResponse(req.getNickname());
    }


    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the server.responses package, one Class for each
     * @param req the response received by the Server from the Client
     */


}
