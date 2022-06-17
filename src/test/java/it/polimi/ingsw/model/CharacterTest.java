package it.polimi.ingsw.model;

import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.controller.GameController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static it.polimi.ingsw.model.Color.GREEN;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterTest {

    @Test
    @DisplayName("General test for Character Class")
    void actionsTest() {
        // TODO
        // STILL NEED TO TEST CHARACTER 7 AND 12
        GameController gameController;
        String nickname;

        gameController = new GameController();
        nickname = "asd";
        gameController.startGame(2, nickname, true);
        gameController.addPlayer("second");

        Player player = gameController.getCurrentGame().getPlayerByNickname(nickname);

        Character[] characters = gameController.getCurrentGame().getAllCharacters();
        PlayCharacterRequest req;

        int i = 0;
        for(Character character : characters){
            switch (character.getEffectType()){
                case "move" -> {
                    List<Integer> studentsInOrigin = new ArrayList<>();
                    if(character.getEffectSource().equals("character")) {
                        //from character [char 1]
                        if(character.getSetupObject().equals("student")) {
                            //[char 1]
                            studentsInOrigin.add(character.getStudents().stream().toList().get(0));
                        }
                    } else {
                        //from playerboard
                        studentsInOrigin.add(player.getPlayerBoard().getStudents().stream().toList().get(0));
                    }
                    List<Integer> targetPieces = new ArrayList<>();
                    if(character.getEffectTarget().equals("island")){
                        //to island 0 [char 1]
                        targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                    } else if(character.getEffectTarget().equals("dining_room")){
                        //to dining [char 11]
                        targetPieces.add(player.getPlayerBoard().getPieceID());
                    } else {
                        //to playerboard
                        targetPieces.add(player.getPlayerBoard().getPieceID());
                    }
                    req = new PlayCharacterRequest(i, nickname, studentsInOrigin, targetPieces);
                }
                case "exchange" -> {
                    //[char 10]
                    List<Integer> studentsInOrigin = new ArrayList<>();
                    List<Integer> studentsInTarget = new ArrayList<>();
                    List<Integer> originPieces = new ArrayList<>();
                    List<Integer> targetPieces = new ArrayList<>();
                    //[char 10]
                    if(character.getEffectObject().equals("student")){
                        //[char 10]
                        if(character.getEffectSource().equals("entrance")){
                            List<Integer> students = player.getPlayerBoard().getStudents().stream().toList();
                            studentsInOrigin.add(students.get(0));
                            originPieces.add(player.getPlayerBoard().getPieceID());
                            studentsInOrigin.add(students.get(1));
                            originPieces.add(player.getPlayerBoard().getPieceID());
                        }
                        //[char 10]
                        if(character.getEffectTarget().equals("dining_room")){
                            List<Integer> students = player.getPlayerBoard().getAllDiningRoomStudents(GREEN);
                            studentsInTarget.add(students.get(0));
                            targetPieces.add(player.getPlayerBoard().getPieceID());
                            studentsInTarget.add(students.get(1));
                            targetPieces.add(player.getPlayerBoard().getPieceID());
                        }
                    }
                    req = new PlayCharacterRequest(i, nickname, studentsInOrigin, studentsInTarget, originPieces, targetPieces);
                }
                case "add" -> {
                    //If nothing more is needed [char 2, 4, 5, 6, 8]
                    if (character.getEffectCondition().equals("any")) {
                        //[char 5]
                        if(character.getEffectTarget().equals("island")){
                            List<Integer> targetPieces = new ArrayList<>();
                            //First island
                            targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                            req = new PlayCharacterRequest(i, nickname, targetPieces);
                        } else {
                            //[char 2, 4, 6, 8]
                            req = new PlayCharacterRequest(i, nickname);
                        }
                    //If color is needed [char 9]
                    } else if(character.getEffectCondition().equals("color")) {
                        Color targetColor = GREEN;
                        req = new PlayCharacterRequest(i, nickname, targetColor);
                    }else {
                        req = null;
                    }
                }
                case "resolve" -> {
                    List<Integer> targetPieces = new ArrayList<>();
                    //[char 3]
                    if(character.getEffectTarget().equals("island")) {
                        targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                    }
                    req = new PlayCharacterRequest(i, nickname, targetPieces);
                }
                default -> {
                    req = null;
                }
            }
            try {
                character.setGameController(gameController);
                character.effect(req);
                System.out.println("DONEEEEEEEEE");
            } catch (NullPointerException e) {
                System.out.println("BOI you fucked up");
                e.printStackTrace();
            }
            i++;
        }
    }
}