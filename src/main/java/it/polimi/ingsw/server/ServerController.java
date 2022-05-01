package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientHandler;
import it.polimi.ingsw.server.responses.AssistantPlayedResponse;
import it.polimi.ingsw.server.responses.CharacterPlayedResponse;
import it.polimi.ingsw.server.responses.MotherNatureMovedResponse;
import it.polimi.ingsw.server.responses.StudentMovedResponse;

/**
 * Controller used by the Server
 */
public class ServerController implements ServerResponseHandler {

    private final ClientHandler clientHandler;

    public ServerController(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the server.responses package, one Class for each
     * @param res the response received by the Server from the Client
     */
    @Override
    public void handle(AssistantPlayedResponse res) {

    }

    @Override
    public void handle(CharacterPlayedResponse res) {

    }

    @Override
    public void handle(MotherNatureMovedResponse res) {

    }

    @Override
    public void handle(StudentMovedResponse res) {

    }
}
