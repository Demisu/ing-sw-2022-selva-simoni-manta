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

/**
 * Controller used by the Client
 */
public class ClientController implements ServerResponseHandler {

    private final Client client;

    private String nickname = "";

    //Game info
    private Game gameInfo;
    private GamePhase gamePhase;
    private Player playerInfo;
    private ArrayList<Character> characters;
    private List<Island> islands;
    private List<Cloud> clouds;
    private ArrayList<SchoolBoard> schoolBoards;

    /**
     * the Client's View, intended as MVC
     */
    private final ClientView view;

    /**
     * Creates a client controller with the relative View type, and binds the two together
     *
     * @param client client for connection
     * @param ui ui type (GUI or CLI)
     */
    public ClientController(Client client, String ui) {
        this.client = client;
        if(ui.equals("GUI")) {
            this.view = new GUI(this);
            this.view.setClientController(this);
        } else {
            this.view = new CLI(this);
        }
    }

    /**
     * Run method to keep the CLI alive
     *
     * @throws IOException IOException
     */
    public void run() throws IOException {

        //Setup (from now on, only CLI)
        CLI cli = (CLI) view;
        cli.setupPhase();
        cli.waitGameStartPhase();

        getModelInfo();
        if(gamePhase.equals(GamePhase.ACTION)){
            cli.actionPhase();
        }

        //Game phases
        do{
            cli.planningPhase();
            cli.actionPhase();
        } while(client.isConnected());

        //Game ended
        System.exit(0);
    }

    /**
     * Closes client connection
     */
    public void closeConnection() {
        try {
            client.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param nickname nickname to set
     * @return SetNicknameResponse, see this.handle(SetNickNameResponse) documentation for the return value
     */
    public Integer setPlayerNickname(String nickname) {
        this.nickname = nickname;
        client.clientRequest(new SetNicknameRequest(nickname));
        return this.handle((SetNicknameResponse) client.clientResponse());
    }

    /**
     * @param number number of players
     * @param expertMode flag for expert mode enabling
     */
    public void setPlayerNumber(Integer number, Boolean expertMode) {
        client.clientRequest(new SetPlayerNumberRequest(nickname, number, expertMode));
        this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * Asks the server for an updated model
     */
    public void getModelInfo() {
        client.clientRequest(new GetUpdatedBoardRequest(nickname));
        this.handle((GetUpdatedBoardResponse) client.clientResponse());
    }

    /**
     * Moves MotherNature
     * @param movements the amount of movements to do
     * @return result (success)
     */
    public Boolean moveMotherNature(Integer movements) {
        client.clientRequest(new MoveMotherNatureRequest(movements, nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @param student student to move
     * @param source source piece id
     * @param target target piece id
     * @return result (success)
     */
    public Boolean moveStudent(Integer student, Integer source, Integer target){
        client.clientRequest(new MoveStudentRequest(student, source, target, nickname));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @param requestBody full request to be interpreted by the character
     * @return result (success)
     */
    public Boolean playCharacter(PlayCharacterRequest requestBody){
        client.clientRequest(requestBody);
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @param number index of the assistant to be played
     * @return result (success)
     */
    public Boolean playAssistant(Integer number){
        client.clientRequest(new PlayAssistantRequest(nickname, number));
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * Queue the client to wait the game start
     *
     * @return result (success)
     */
    public Boolean waitGameStart(){
        client.clientRequest(new WaitingRequest());
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @return if the game has started
     */
    public Boolean isGameStarted(){
        client.clientRequest(new GameStartedRequest());
        return this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * Method used to pass turn
     */
    public void passTurn(){
        client.clientRequest(new PassTurnRequest(nickname));
        this.handle((OperationResultResponse) client.clientResponse());
    }

    /**
     * @param res response from the server
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

    /**
     * @param res response from the server
     * @return result of the response (success)
     */
    @Override
    public Boolean handle(OperationResultResponse res) {

        System.out.println(res.getMessage());
        return res.getResult();
    }

    /**
     * @param res updated model (response) from the server
     */
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

    /**
     * @return playerInfo
     */
    public Player getPlayerInfo() {
        return playerInfo;
    }

    /**
     * @return characters
     */
    public ArrayList<Character> getCharacters() {
        return characters;
    }

    /**
     * @return islands
     */
    public List<Island> getIslands() {
        return islands;
    }

    /**
     * @return clouds
     */
    public List<Cloud> getClouds() {
        return clouds;
    }

    /**
     * @return schoolBoards
     */
    public ArrayList<SchoolBoard> getSchoolBoards() {
        return schoolBoards;
    }

    /**
     * @return gamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * @param nickname nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return game reference
     */
    public Game getGameInfo() {
        return gameInfo;
    }

    /**
     * @return client reference
     */
    public Client getClient() {
        return client;
    }
}
