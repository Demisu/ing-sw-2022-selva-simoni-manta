package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

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

    public void moveStudent(Integer student, StudentAccessiblePiece origin, StudentAccessiblePiece target){
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
        //ask model to process
    }

    /*-------*/
    /* CARDS */
    /*-------*/

    public void playAssistant(Integer playerID, Integer assistantCode){
        //ask model to process
    }

    public void playCharacter(Integer playerID, Integer characterNumber){
        //ask model to process
    }
    
}
