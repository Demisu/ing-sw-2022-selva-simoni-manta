package it.polimi.ingsw.client;

import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.SchoolBoard;
import it.polimi.ingsw.server.responses.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Controller used by the Client
 */

public class ClientController implements ServerResponseHandler {

    private final Client client;

    private Thread receiver;

    //Game info
    Player playerInfo;
    ArrayList<Character> characters;
    List<Island> islands;
    Set<Cloud> clouds;
    ArrayList<SchoolBoard> schoolBoards;

    /**
     * the Client's View, intended as MVC
     */
    private final ClientView view;

    public ClientController(Client client) {
        this.client = client;
        this.view = new ClientView(this);
    }

    public void run() throws IOException {

        //Setup
        view.nicknamePhase();
        view.testingPhase();
        /*

        //Game phases
        do{
            view.waitTurnPhase();
            view.planningPhase();
            view.actionPhase();
        } while(client.isConnected());

        //Game ended
        receiver.interrupt();*/
    }


    /**
     * Methods corresponding 1:1 to the Requests the Client can do
     * Responses are defined in the client.requests package, one Class for each
     */

    /**
     * Notifies the Player's Round on the executing Client has terminated
     * @param status must be set to TRUE for the request to be correctly processed
     *               by the server
     */
    public void playerRoundEnded(Boolean status) {
        client.clientRequest(new PlayerRoundEndedRequest(status));
    }

    public Boolean setPlayerNickname(String nickname) {
        client.clientRequest(new SetNicknameRequest(nickname));
        return this.handle((SetNicknameResponse) client.clientResponse());
        //return ((SetNicknameResponse) client.clientResponse()).getNickname();
    }

    public Boolean setPlayerNumber(String nickname, Integer number) {
        client.clientRequest(new SetPlayerNumberRequest(nickname, number));
        return this.handle((OperationResultResponse) client.clientResponse());
        //return ((SetNicknameResponse) client.clientResponse()).getNickname();
    }

    public void getModelInfo(String nickname) {
        client.clientRequest(new GetUpdatedBoardRequest(nickname));
        this.handle((GetUpdatedBoardResponse) client.clientResponse());
        System.out.println("SIUUUUUUUUUUUUUUUUUUUUUM");
    }

    /**
     * Moves MotherNature
     * @param movements the amount of movements to do
     */
    public Boolean moveMotherNature(Integer movements) {
        client.clientRequest(new MoveMotherNatureRequest(movements));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean moveStudent(Integer student, Integer source, Integer target){
        client.clientRequest(new MoveStudentRequest(student, source, target));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean playCharacter(Integer number){
        client.clientRequest(new PlayCharacterRequest(number));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean playAssistant(String nickname, Integer number){
        client.clientRequest(new PlayAssistantRequest(nickname, number));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean waitTurn() {
        client.clientRequest(new WaitingRequest());
        return this.handle((OperationResultResponse) client.clientResponse());
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
    public Boolean handle(SetNicknameResponse res) {
        return res.getNeedPlayerNumber();
    }

    @Override
    public Boolean handle(OperationResultResponse res) {
        System.out.println(res.getMessage());
        return res.getResult();
    }

    @Override
    public void handle(GetUpdatedBoardResponse res) {
        playerInfo = res.getPlayerInfo();
        characters = res.getCharacters();
        islands = res.getIslands();
        clouds = res.getClouds();
        schoolBoards = res.getSchoolBoards();
    }

    public Player getPlayerInfo() {
        return playerInfo;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public Set<Cloud> getClouds() {
        return clouds;
    }

    public ArrayList<SchoolBoard> getSchoolBoards() {
        return schoolBoards;
    }

    //    @Override
//    public void handle(RequestSuccessfulResponse res) {
//
//    }


}
