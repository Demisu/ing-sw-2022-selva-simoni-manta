package it.polimi.ingsw.client;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.requests.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
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

    String nickname = "";

    //Game info
    GamePhase gamePhase;
    Player playerInfo;
    ArrayList<Character> characters;
    List<Island> islands;
    Set<Cloud> clouds;
    ArrayList<SchoolBoard> schoolBoards;

    /**
     * the Client's View, intended as MVC
     */
    private final ClientView view;

    public ClientController(Client client, String ui) {
        this.client = client;
        if(ui.equals("GUI")) {
            this.view = new GUI(this);
        } else {
            this.view = new CLI(this);
        }
    }

    public void run() throws IOException {

        //Setup
        view.setupPhase();
        view.waitGameStartPhase();
        //do{
        //    view.testingPhase();
        //} while(client.isConnected());

        //Game phases
        do{
            view.planningPhase();
            view.actionPhase();
        } while(client.isConnected());

        //Game ended
        receiver.interrupt();
    }


    /**
     * Methods corresponding 1:1 to the Requests the Client can do
     * Responses are defined in the client.requests package, one Class for each
     */

    public Boolean setPlayerNickname(String nickname) {
        this.nickname = nickname;
        client.clientRequest(new SetNicknameRequest(nickname));
        return this.handle((SetNicknameResponse) client.clientResponse());
        //return ((SetNicknameResponse) client.clientResponse()).getNickname();
    }

    public Boolean setPlayerNumber(Integer number, Boolean expertMode) {
        client.clientRequest(new SetPlayerNumberRequest(nickname, number, expertMode));
        return this.handle((OperationResultResponse) client.clientResponse());
        //return ((SetNicknameResponse) client.clientResponse()).getNickname();
    }

    public void getModelInfo() {
        client.clientRequest(new GetUpdatedBoardRequest(nickname));
        this.handle((GetUpdatedBoardResponse) client.clientResponse());
        System.out.println("Objects updated!");
    }

    /**
     * Moves MotherNature
     * @param movements the amount of movements to do
     */
    public Boolean moveMotherNature(Integer movements) {
        client.clientRequest(new MoveMotherNatureRequest(movements, nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean moveStudent(Integer student, Integer source, Integer target){
        client.clientRequest(new MoveStudentRequest(student, source, target, nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean playCharacter(Integer number){
        client.clientRequest(new PlayCharacterRequest(number, nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean playAssistant(Integer number){
        client.clientRequest(new PlayAssistantRequest(nickname, number));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean waitGameStart(){
        client.clientRequest(new WaitingRequest());
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean passTurn(){
        client.clientRequest(new PassTurnRequest(nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
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
        playerInfo = res.getGameInfo().getPlayerByNickname(nickname);
        if(res.getGameInfo().isExpertMode()) {
            characters = new ArrayList<>(List.of(res.getGameInfo().getAllCharacters()));
        }
        islands = res.getGameInfo().getIslands();
        clouds = res.getGameInfo().getClouds();
        schoolBoards = new ArrayList<>();
        res.getGameInfo().getPlayers().forEach(player -> schoolBoards.add(player.getPlayerBoard()));
        gamePhase = res.getGameInfo().getCurrentPhase();
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

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
