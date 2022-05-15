package it.polimi.ingsw.client;

import it.polimi.ingsw.client.requests.MoveMotherNatureRequest;
import it.polimi.ingsw.client.requests.PlayerRoundEndedRequest;
import it.polimi.ingsw.client.requests.SetNicknameRequest;
import it.polimi.ingsw.server.ServerResponseHandler;
import it.polimi.ingsw.server.responses.*;

import java.io.IOException;

/**
 * Controller used by the Client
 */

public class ClientController implements ServerResponseHandler {

    private final Client client;

    private Thread receiver;

    /**
     * the Client's View, intended as MVC
     */
    private final ClientView view;

    public ClientController(Client client) {
        this.client = client;
        this.view = new ClientView(this);
    }

    public void run() throws IOException {
        view.nicknamePhase();
        receiver.interrupt();
    }


    /**
     * Methods corresponding 1:1 to the Requests the Client can do
     * Responses are defined in the client.requests package, one Class for each
     */

    /**
     * Moves MotherNature
     * @param movements the amount of movements to do
     */
    public void moveMotherNature(Integer movements) {
        client.clientRequest(new MoveMotherNatureRequest(movements));
    }

    /**
     * Notifies the Player's Round on the executing Client has terminated
     * @param status must be set to TRUE for the request to be correctly processed
     *               by the server
     */
    public void playerRoundEnded(Boolean status) {
        client.clientRequest(new PlayerRoundEndedRequest(status));
    }


    public String setPlayerNickname(String nickname) {
        client.clientRequest(new SetNicknameRequest(nickname));
        return ((SetNicknameResponse) client.clientResponse()).getNickname();
    }

    /**
     * Properly dispatches the received response to the correct method
     * Requests are defined in the client.responses package, one Class for each
     * @param res the response received by the Client from the Server
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

    @Override
    public void handle(SetNicknameResponse res) {

    }



//    @Override
//    public void handle(RequestSuccessfulResponse res) {
//
//    }


}
