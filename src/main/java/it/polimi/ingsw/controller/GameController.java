package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.ArrayList;
import java.util.List;

public class GameController {

    private static Game referenceGame;
    Game currentGame;

    /*------*/
    /* GAME */
    /*------*/

    public void startGame(Integer playerNumber, String nicknameOfCreator){
        currentGame = new Game(playerNumber, nicknameOfCreator);
        referenceGame = currentGame;
    }

    public void resetModifiers(){

        Game.setAllStudentsValue(1);
        Game.setTowerValue(1);
        Game.setInfluenceModifier(0);
        Game.setMotherNatureMovements(0);
        Game.setStudentsInDiningModifier(0);

        for (Player player : currentGame.getPlayers()) {
            player.setActiveCharacter(false);
            //player.setLastAssistantPlayed(null);
        }
    }

    /*----------*/
    /* MOVEMENT */
    /*----------*/

    public void moveStudent(Integer student, Integer originID, Integer targetID) {

        StudentAccessiblePiece origin = currentGame.getStudentAccessiblePieceByID(originID);
        StudentAccessiblePiece target = currentGame.getStudentAccessiblePieceByID(targetID);

        //If movement is from school board entrance to dining room (same id)
        if(origin.getPieceID() == target.getPieceID()){

            ( (SchoolBoard) origin).studentToDining(student);
            //Check if someone wins a professor
            this.checkProfessorChange(StudentAccessiblePiece.colorOfStudent(student));

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
        boolean unified = false;
        int refIndex;
        Island currentIsland = currentGame.getIslandByID(islandID);

        do {

            refIndex = islands.indexOf(currentIsland);
            Island nextIsland = islands.get((refIndex + 1) % (islands.size()));
            Island previousIsland;
            if(refIndex==0){
                previousIsland = islands.get(islands.size()-1);
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

    /*-------*/
    /* CARDS */
    /*-------*/

    public void playAssistant(Integer playerID, Integer assistantCode){

        Player p = currentGame.getPlayerById(playerID);
        p.removeAssistant(p.getAssistantById(assistantCode)); //handles setting last assistant played property as well
    }

    public void playAssistant(String playerNickname, Integer assistantCode){

        Player p = currentGame.getPlayerByNickname(playerNickname);
        p.removeAssistant(p.getAssistantById(assistantCode)); //handles setting last assistant played property as well
    }

    public void playCharacter(Integer playerID, Integer characterNumber){

        Character playedCharacter =  currentGame.getCharacter(characterNumber);
        //REMEMBER TO REVERT THIS AFTER ROUND ENDS
        currentGame.getPlayerById(playerID).setActiveCharacter(true);
        //Potentially really bad
        playedCharacter.setGameController(this);

        playedCharacter.effect();

        if(playedCharacter.getHasIncreasedCost()){
            playedCharacter.setCost(playedCharacter.getCost() + 1);
        }

        playedCharacter.setHasBeenUsed(true);
    }

    /*------*/
    /* MISC */
    /*------*/

    public Boolean addPlayer(String nickname){
        return currentGame.addPlayer(nickname);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public static Game getReferenceGame() {
        return referenceGame;
    } //Might be deleted in the future

    public Integer getPlayerNumber() {
        return currentGame.getPlayers().size();
    }
}
