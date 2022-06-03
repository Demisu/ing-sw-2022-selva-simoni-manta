package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.*;

import java.util.List;

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
        Island motherNatureIsland = currentGame.getMotherNatureIsland();
        int currentID;

        currentID = islands.indexOf(motherNatureIsland);
        motherNatureIsland.setMotherNature(false);

        // Mod to number of island (default 12) + 1 in the game to avoid going out of boundaries
        int refIndex = (currentID+steps)%(islands.size());
        Island currentIsland = islands.get(refIndex);
        currentIsland.setMotherNature(true);

        // Resolve that island
        this.resolveIsland(currentIsland.getPieceID());
    }

    public void resolveIsland(Integer islandID){

        Island island = currentGame.getIslandByID(islandID);
        currentGame.resolveIsland(island);
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
        currentGame.nextPlayer();
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