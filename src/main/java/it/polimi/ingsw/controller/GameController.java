package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

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

    /*----------*/
    /* MOVEMENT */
    /*----------*/

    public void moveStudent(Integer student, Integer originID, Integer targetID) {

        StudentAccessiblePiece origin = currentGame.getStudentAccessiblePieceByID(originID);
        StudentAccessiblePiece target = currentGame.getStudentAccessiblePieceByID(targetID);
        origin.removeStudent(student);
        target.addStudent(student);

        //Maybe check if someone wins a professor?
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
            Island previousIsland = islands.get((refIndex - 1) % (islands.size()));

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
    }

    public Integer getPlayerNumber() {
        return currentGame.getPlayers().size();
    }
}
