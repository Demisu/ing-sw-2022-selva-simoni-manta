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
        islands.get( (currentID+steps)%(islands.size()) ).setMotherNature(true);

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
        playedCharacter.effect();
        if(playedCharacter.getHasIncreasedCost()){
            playedCharacter.setCost(playedCharacter.getCost() + 1);
        }
        playedCharacter.setHasBeenUsed(true);
    }

    /*------*/
    /* MISC */
    /*------*/

    //Moves all the content of Island b to Island b, then deletes island b from the game table
    public void unifyIslands(Island a, Island b){

        a.getStudents().addAll(b.getStudents());
        a.setTowersNumber(a.getTowersNumber() + b.getTowersNumber());
        a.setNoEntry(a.getNoEntry() + b.getNoEntry());
        if(b.isMotherNature()){
            a.setMotherNature(true);
        }
        currentGame.getIslands().remove(b);
    }

    public Boolean addPlayer(String nickname){
        return currentGame.addPlayer(nickname);
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public static Game getReferenceGame() {
        return referenceGame;
    }
}
