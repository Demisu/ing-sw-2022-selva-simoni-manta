package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.*;
import java.util.List;

public class GameController {

    Game currentGame;

    /*------*/
    /* GAME */
    /*------*/

    public void startGame(Integer playerNumber, String nicknameOfCreator){
        currentGame = new Game(playerNumber, nicknameOfCreator);
    }

    /*----------*/
    /* MOVEMENT */
    /*----------*/

    public void moveStudent(Integer student, StudentAccessiblePiece origin, StudentAccessiblePiece target) {
        origin.removeStudent(student);
        target.addStudent(student);
        return;
    }

    public void moveProfessor(Color professorColor, SchoolBoard origin, SchoolBoard target){
        origin.setProfessor(professorColor, false);
        target.setProfessor(professorColor, true);
        return;
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
        islands.get( (currentID+steps)%(currentGame.getIslands().size()+1) ).setMotherNature(true);

    }

    /*-------*/
    /* CARDS */
    /*-------*/

    public void playAssistant(Integer playerID, Integer assistantCode){
        Player p = currentGame.getPlayerById(playerID);
        p.removeAssistantById(assistantCode); //handles setting last assistant played property as well
    }

    public void playCharacter(Integer playerID, Integer characterNumber){
        //ask model to process
    }

    /*------*/
    /* MISC */
    /*------*/

    public void unifyIslands(Island a, Island b){
        a.getStudents().addAll(b.getStudents());
        a.setTowersNumber(a.getTowersNumber() + b.getTowersNumber());
        if(b.isMotherNature()){
            a.setMotherNature(true);
        }
    }

    public Game getCurrentGame() {
        return currentGame;
    }
}
