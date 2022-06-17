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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller used by the Client
 */

public class ClientController implements ServerResponseHandler {

    private final Client client;
    private Thread receiver;

    String nickname = "";

    //Game info
    Game gameInfo;
    GamePhase gamePhase;
    Player playerInfo;
    ArrayList<Character> characters;
    List<Island> islands;
    List<Cloud> clouds;
    ArrayList<SchoolBoard> schoolBoards;

    /**
     * the Client's View, intended as MVC
     */
    private final ClientView view;

    public ClientController(Client client, String ui) {
        this.client = client;
        if(ui.equals("GUI")) {
            this.view = new GUI(this);
            this.view.setClientController(this);
        } else {
            this.view = new CLI(this);
        }
    }

    public void run() throws IOException {

        //Setup
        view.setupPhase();
        view.waitGameStartPhase();

        //ScheduledExecutorService modelUpdater = Executors.newSingleThreadScheduledExecutor();
        //modelUpdater.scheduleAtFixedRate( () -> client.clientRequest(new GetUpdatedBoardRequest(nickname)), 0, 2, TimeUnit.SECONDS);

        //Game phases
        do{
            view.planningPhase();
            view.actionPhase();
        } while(client.isConnected());

        //Game ended
        receiver.interrupt();
    }

    public void closeConnection() {
        try {
            client.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Methods corresponding 1:1 to the Requests the Client can do
     * Responses are defined in the client.requests package, one Class for each
     */

    public Integer setPlayerNickname(String nickname) {
        this.nickname = nickname;
        client.clientRequest(new SetNicknameRequest(nickname));
        return this.handle((SetNicknameResponse) client.clientResponse());
    }

    public Boolean setPlayerNumber(Integer number, Boolean expertMode) {
        client.clientRequest(new SetPlayerNumberRequest(nickname, number, expertMode));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public void getModelInfo() {
        client.clientRequest(new GetUpdatedBoardRequest(nickname));
        this.handle((GetUpdatedBoardResponse) client.clientResponse());
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

    public Boolean isGameStarted(){
        client.clientRequest(new GameStartedRequest());
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    public Boolean passTurn(){
        client.clientRequest(new PassTurnRequest(nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @param res
     * @return Integer: 0 if the server DOES NOT need player number
     *                  1 if the server NEEDS player number
     *                  2 if the action FAILED (game full or error)
     */
    @Override
    public Integer handle(SetNicknameResponse res) {
        //Failure
        if(!res.getSuccess())
            return 2;
        //Need player number
        if(res.getNeedPlayerNumber())
            return 1;
        //Does not need player number
        return 0;
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
        gameInfo = res.getGameInfo();
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

    public List<Cloud> getClouds() {
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

    public Game getGameInfo() {
        return gameInfo;
    }

    public Client getClient() {
        return client;
    }
}
