package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.*;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

/**
 * Class for the Main Game Controller
 */
public class GameController {

    private Game currentGame;
    private Integer skipTurnDelay = 5;

    /*------*/
    /* GAME */
    /*------*/

    /**
     * Starts a new game and saves a static reference
     *
     * @param playerNumber Number of players of the game
     * @param nicknameOfCreator Creator of the game
     * @param expertMode Set to true if expert mode needs to be on (i.e.: adds characters)
     */
    public void startGame(Integer playerNumber, String nicknameOfCreator, Boolean expertMode){
        currentGame = new Game(playerNumber, nicknameOfCreator, expertMode);
    }

    /*----------*/
    /* MOVEMENT */
    /*----------*/


    /**
     * Moves the student from the origin to the target, can handle movement between all StudentAccessiblePiece objects.
     * Automatically understands if both the source and the target are the same school board, and in this case moves
     * the student from the entrance to the dining room.
     *
     * @param student The student to move
     * @param originID The origin piece ID
     * @param targetID The target piece ID
     */
    public void moveStudent(Integer student, Integer originID, Integer targetID) {

        StudentAccessiblePiece origin = currentGame.getStudentAccessiblePieceByID(originID);
        StudentAccessiblePiece target = currentGame.getStudentAccessiblePieceByID(targetID);

        //If movement is from school board entrance to dining room (same id)
        if(origin.getPieceID().equals(target.getPieceID())){

            //Move student
            ( (SchoolBoard) origin).studentToDining(student);
            //If this adds 3rd, 6th or 9th student, give the player a coin
            if(((SchoolBoard) origin).getDiningRoomStudents(colorOfStudent(student)) % 3 == 0){
                //Search the player
                for (Player player : currentGame.getPlayers()){
                    //If the player owns the origin school board, give him a coin
                    if(player.getPlayerBoard().getPieceID().equals(originID)){
                        player.setCoins(player.getCoins() + 1);
                        break;
                    }
                }
            }

            //Check if someone wins a professor
            this.checkProfessorChange(colorOfStudent(student));

        }else{

            origin.removeStudent(student);
            target.addStudent(student);
        }
    }

    /**
     * Checks if a professor must change owner, depending on the number of students in the dining rooms
     *
     * @param color color of the professor to check
     */
    public void checkProfessorChange(Color color){

        SchoolBoard winningBoard = null;
        SchoolBoard oldBoard = null;
        int maxFound = -1;

        for (Player player : currentGame.getPlayers()) {

            if(player.getPlayerBoard().getProfessors()[StudentAccessiblePiece.indexOfColor(color)]){
                oldBoard = player.getPlayerBoard();
            }

            //If a certain character is active,
            int currentNumber = player.getPlayerBoard().getDiningRoomStudents(color) +
                            (player.hasActiveCharacter() ?
                            currentGame.getStudentsInDiningModifier() :
                            0);

            if(currentNumber > maxFound){
                maxFound = currentNumber;
                winningBoard = player.getPlayerBoard();
            }else if(player.getPlayerBoard().getDiningRoomStudents(color) == maxFound){
                maxFound = -1;
                winningBoard = null;
            }
        }

        if(winningBoard != null){
            if(oldBoard != null){
                this.moveProfessor(color, oldBoard.getPieceID(), winningBoard.getPieceID());
            }else{
                winningBoard.setProfessor(color, true);
            }
        }
    }

    /**
     * Moves the professor from the origin to the target, can only handle movement between school boards.
     *
     * @param professorColor The professor to move
     * @param originID The origin piece ID
     * @param targetID The target piece ID
     */
    public void moveProfessor(Color professorColor, Integer originID, Integer targetID){

        SchoolBoard origin = currentGame.getSchoolBoardByID(originID);
        SchoolBoard target = currentGame.getSchoolBoardByID(targetID);
        origin.setProfessor(professorColor, false);
        target.setProfessor(professorColor, true);
    }

    /**
     * Moves mother nature of n steps. Handles no entry tiles on target island (if present).
     *
     * @param steps number of steps (aka islands) that mother nature must move
     */
    public void moveMotherNature(Integer steps){

        List<Island> islands = currentGame.getIslands();
        Island motherNatureIsland = currentGame.getMotherNatureIsland();
        int currentID;

        currentID = islands.indexOf(motherNatureIsland);
        motherNatureIsland.setMotherNature(false);

        // Mod to number of island (default 12) + 1 in the game to avoid going out of boundaries
        int refIndex = (currentID+steps)%(islands.size());
        Island currentIsland = islands.get(refIndex);
        currentIsland.setMotherNature(true);

        //Sets Mother nature as already moved in this turn
        currentGame.setMovedMotherNatureInTurn(true);

        // If there are no entry tiles, remove one of them and don't resolve the island
        if(currentIsland.getNoEntry() > 0){
            currentIsland.setNoEntry(currentIsland.getNoEntry() - 1);
            for (Character character : currentGame.getAllCharacters()){
                // Place the removed no entry on the owner character
                if(character.getSetupObject().equals("no_entry")){
                    character.setNoEntryNumber(character.getNoEntryNumber() + 1);
                    break;
                }
            }
        } else {
            // Resolve that island
            this.resolveIsland(currentIsland.getPieceID());
        }
    }


    /**
     * Handles getting the island reference by ID, then calls Game.resolveIsland(Island)
     *
     * @param islandID ID of the island to be resolved
     */
    public void resolveIsland(Integer islandID){

        Island island = currentGame.getIslandByID(islandID);
        currentGame.resolveIsland(island);
    }

    /*-------*/
    /* CARDS */
    /*-------*/

    /**
     * Parses the playerID to its nickname and calls playAssistant(String, Integer)
     *
     * @param playerID ID of the player that is playing th assistant
     * @param assistantNumber Number (index of deck) of the assistant tu be played
     */
    public void playAssistant(Integer playerID, Integer assistantNumber){

        playAssistant(currentGame.getPlayerById(playerID).getNickname(), assistantNumber);
    }

    /**
     * Handles getting the player reference by nickname (unique), then calls Player.removeAssistant(Assistant)
     * which handles the rest
     *
     * @param playerNickname Nickname of the player that is playing th assistant
     * @param assistantNumber Number (index of deck) of the assistant tu be played
     */
    public void playAssistant(String playerNickname, Integer assistantNumber){

        Player p = currentGame.getPlayerByNickname(playerNickname);
        p.removeAssistant(p.getDeck().get(assistantNumber)); //handles setting last assistant played property as well

        //May be changed
        currentGame.nextPlayer();
    }

    /**
     * Prepares the character to be played, sets its cost (if increased) and calls his effect.
     * The Request is automatically interpreted by the character.
     *
     * @param fullRequest Body of the full request
     */
    public void playCharacter(PlayCharacterRequest fullRequest){

        Character playedCharacter =  currentGame.getCharacter(fullRequest.getCharacterNumber());
        Player player = currentGame.getPlayerByNickname(fullRequest.getNickname());

        player.setActiveCharacter(true);
        playedCharacter.setGameController(this);
        playedCharacter.effect(fullRequest);
        player.setCoins(player.getCoins() - 1);

        if(playedCharacter.getHasIncreasedCost()){
            playedCharacter.setCost(playedCharacter.getCost() + 1);
        }

        playedCharacter.setHasBeenUsed(true);
    }

    /*------*/
    /* MISC */
    /*------*/

    /**
     * Adds the player to the game, or reconnects him to his inactive account
     *
     * @param nickname nickname of the player to be added
     * @return success boolean
     */
    public Boolean addPlayer(String nickname){
        for (Player player : currentGame.getPlayers()) {
            if(player.getNickname().equals(nickname)){
                //Player already in game
                player.setActive(true);
                return true;
            }
        }
        return currentGame.addPlayer(nickname);
    }

    /**
     * @return currentGame
     */
    public synchronized Game getCurrentGame() {
        return currentGame;
    }

    /**
     * @return the number of players
     */
    public Integer getPlayerNumber() {
        return currentGame.getPlayers().size();
    }

    /**
     * Sets the player status to inactive. Starts a timer of 5 seconds to wait his reconnection,
     * otherwise skips his turn (if he is the current player). If the player did not play an assistant during the
     * planning phase, this sets a dummy assistant to avoid null pointer exceptions
     *
     * @param nickname nickname of the player to be set inactive
     */
    public void setInactive(String nickname){
        if(nickname == null || currentGame.getPlayerByNickname(nickname) == null){
            //Invalid player
            return;
        }

        Player player = currentGame.getPlayerByNickname(nickname);
        player.setActive(false);

        //5sec timer after the player left, then his turn is passed
        System.out.println("Started 5sec timer for player " + nickname + " (Disconnected)");
        ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
        timer.schedule(() -> {
            if (!currentGame.getPlayerByNickname(nickname).isActive()) {
                System.out.println("Timer ended, the player did not reconnect. Skipping his turn in case");
                if(currentGame.getCurrentPlayer().equals(nickname)){
                    if(player.getLastAssistantPlayed() == null) {
                        currentGame.getPlayerByNickname(nickname).setLastAssistantPlayed(new Assistant(11, 0, -1));
                    }
                    currentGame.nextPlayer();
                }
            }}, skipTurnDelay, TimeUnit.SECONDS);
    }

    /**
     * @param skipTurnDelay Delay for the afk timer
     */
    public void setSkipTurnDelay(Integer skipTurnDelay) {
        this.skipTurnDelay = skipTurnDelay;
    }
}