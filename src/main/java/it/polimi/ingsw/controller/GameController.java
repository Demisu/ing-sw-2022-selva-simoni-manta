package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

public class GameController {

    private static Game referenceGame;
    private Game currentGame;

    /*------*/
    /* GAME */
    /*------*/

    public void startGame(Integer playerNumber, String nicknameOfCreator, Boolean expertMode){
        currentGame = new Game(playerNumber, nicknameOfCreator, expertMode);
        currentGame.setCurrentPhase(GamePhase.SETUP);
        referenceGame = currentGame;
    }

    /*----------*/
    /* MOVEMENT */
    /*----------*/

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
                            Game.getStudentsInDiningModifier() :
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

    public void moveProfessor(Color professorColor, Integer originID, Integer targetID){

        SchoolBoard origin = currentGame.getSchoolBoardByID(originID);
        SchoolBoard target = currentGame.getSchoolBoardByID(targetID);
        origin.setProfessor(professorColor, false);
        target.setProfessor(professorColor, true);
    }

    public void moveMotherNature(Integer steps){

        List<Island> islands = currentGame.getIslands();
        int currentID = 0;

        for (Island island : islands) {
            if(island.isMotherNature()){
                currentID = islands.indexOf(island);
                islands.get(currentID).setMotherNature(false);
                break;
            }
        }

        // Mod to number of island (default 12) + 1 in the game to avoid going out of boundaries
        int refIndex = (currentID+steps)%(islands.size());
        Island currentIsland = islands.get(refIndex);
        currentIsland.setMotherNature(true);

        // Resolve that island
        this.resolveIsland(currentIsland.getPieceID());
    }

    public void resolveIsland(Integer islandID){

        List<Island> islands = currentGame.getIslands();
        boolean unified;
        int refIndex;
        Island currentIsland = currentGame.getIslandByID(islandID);
        currentIsland.resolve(currentGame.getTeams());

        do {
            //Check if there are 3 or less remaining islands. If true, game ends
            if(currentGame.getIslands().size() <= 3){

                //TODO GAME ENDS HERE

            }

            refIndex = islands.indexOf(currentIsland);
            Island nextIsland = islands.get((refIndex + 1) % (islands.size()));
            Island previousIsland;
            if(refIndex==0){
                previousIsland = islands.get(islands.size() - 1);
            }else{
                previousIsland = islands.get((refIndex - 1) % (islands.size()));
            }

            if (currentIsland.getTowersColor() == nextIsland.getTowersColor()) {
                currentGame.unifyIslands(currentIsland, nextIsland);
                unified = true;
            } else if (currentIsland.getTowersColor() == previousIsland.getTowersColor()) {
                currentGame.unifyIslands(currentIsland, previousIsland);
                unified = true;
            } else {
                unified = false;
            }

        } while(unified);
    }

    public void nextPlayer() {

        currentGame.resetModifiers();
        int currentIndex = currentGame.getCurrentTurnOrder().indexOf(
                                                    currentGame.getPlayerByNickname(
                                                            currentGame.getCurrentPlayer()));
        //If current is the last element
        if(currentIndex == currentGame.getCurrentTurnOrder().size() - 1){

            //If all players played an assistant, change to action phase
            if(currentGame.getCurrentPhase().equals(GamePhase.PLANNING)){

                //Update order and move to action order
                updateNextTurnOrder();
                currentGame.setCurrentTurnOrder(currentGame.getActionPhaseOrder());
                currentGame.setCurrentPhase(GamePhase.ACTION);

            } else {
                //If the game bag is empty, the game ends
                if(currentGame.getBagStudents().size() <= 0){

                    //TODO GAME ENDS HERE

                }
                //Action phase ended, next turn starting
                currentGame.setCurrentTurnOrder(currentGame.getNextTurnOrder());
                currentGame.setCurrentPlayer(currentGame.getCurrentTurnOrder().get(0).getNickname());
                currentGame.setCurrentPhase(GamePhase.PLANNING);
                //Refill clouds for new planning phase
                currentGame.turnStartFill();
            }

        } else {
            //Next player
            currentGame.setCurrentPlayer(currentGame.getCurrentTurnOrder().get(currentIndex + 1).getNickname());
        }
    }

    public void updateNextTurnOrder(){

        List<Player> players = currentGame.getPlayers();
        List<Player> actionPhaseOrder = players.stream()
                .sorted(Comparator.comparingInt(Player::getLastAssistantPlayedPriority).reversed())
                .collect(Collectors.toList());

        currentGame.setActionPhaseOrder(actionPhaseOrder);
        Integer firstPlayerIndex = actionPhaseOrder.get(0).getPlayerId();
        List<Player> nextTurnOrder = new ArrayList<>();

        for(int i = 0; i < players.size(); i++){
            //first player = who won the character phase. Next players selected clockwise (following players original list)
            nextTurnOrder.add(players.get( (firstPlayerIndex + i) % players.size() ));
        }

        currentGame.setNextTurnOrder(nextTurnOrder);
    }

    /*-------*/
    /* CARDS */
    /*-------*/

    public void playAssistant(Integer playerID, Integer assistantNumber){

        playAssistant(currentGame.getPlayerById(playerID).getNickname(), assistantNumber);
    }

    public void playAssistant(String playerNickname, Integer assistantNumber){

        Player p = currentGame.getPlayerByNickname(playerNickname);
        p.removeAssistant(p.getDeck().get(assistantNumber)); //handles setting last assistant played property as well

        //May be changed
        this.nextPlayer();
    }

    public void playCharacter(PlayCharacterRequest fullRequest){

        Character playedCharacter =  currentGame.getCharacter(fullRequest.getCharacterNumber());

        currentGame.getPlayerByNickname(fullRequest.getNickname()).setActiveCharacter(true);
        playedCharacter.setGameController(this);

        playedCharacter.effect(fullRequest);

        if(playedCharacter.getHasIncreasedCost()){
            playedCharacter.setCost(playedCharacter.getCost() + 1);
        }

        playedCharacter.setHasBeenUsed(true);

        //May be changed
        //this.nextPlayer();
    }

    /*------*/
    /* MISC */
    /*------*/

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

    public synchronized Game getCurrentGame() {
        return currentGame;
    }

    public static Game getReferenceGame() {
        return referenceGame;
    } //Might be deleted in the future

    public Integer getPlayerNumber() {
        return currentGame.getPlayers().size();
    }
}