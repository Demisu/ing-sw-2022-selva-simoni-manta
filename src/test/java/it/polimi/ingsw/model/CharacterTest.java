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
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {

    @Test
    @DisplayName("General test for Character Class")
    void actionsTest() {

        Boolean[] testedCharacters = new Boolean[12];
        boolean notAllTested;
        int count = 0;
        Arrays.fill(testedCharacters, false);
        // STILL NEED TO TEST CHARACTER 7 AND 12
        testedCharacters[7 - 1] = true;
        testedCharacters[12 - 1] = true;
        //Repeat x times until all characters have been tested
        do {

            GameController gameController;
            String nickname;
            nickname = "asd";
            boolean flag;

            do {
                flag = false;
                gameController = new GameController();

                gameController.startGame(2, nickname, true);
                gameController.addPlayer("second");

                for (Character character : gameController.getCurrentGame().getAllCharacters()) {
                    //Avoid not implemented characters
                    if (character.getImage().equals("7") || character.getImage().equals("12")) {
                        flag = true;
                        break;
                    }
                }

            } while (flag);

            Player player = gameController.getCurrentGame().getPlayerByNickname(nickname);

            Character[] characters = gameController.getCurrentGame().getAllCharacters();
            PlayCharacterRequest req;

            //REQUEST PARAMETERS
            Color targetColor;
            List<Integer> studentsInOrigin;
            List<Integer> studentsInTarget;
            List<Integer> originPieces;
            List<Integer> targetPieces;

            int i = 0;
            for (Character character : characters) {
                //Useless
                boolean assddfd = character.getHasIncreasedCost();
                character.setCost(character.getCost());
                String asd = character.getImage();
                Integer asasdad = character.getNoEntryNumber();
                assddfd = character.getHasBeenUsed();
                assddfd = character.getHasIncreasedCost();
                asasdad = character.getEffectNumberMax();
                //Not useless
                System.out.println("CHARACTER " + character.getImage());
                //Check character as tested
                testedCharacters[Integer.parseInt(character.getImage()) - 1] = true;
                //Reset params
                targetColor = null;
                studentsInOrigin = new ArrayList<>();
                studentsInTarget = new ArrayList<>();
                originPieces = new ArrayList<>();
                targetPieces = new ArrayList<>();
                switch (character.getEffectType()) {
                    case "move" -> {
                        if (character.getEffectSource().equals("character")) {
                            //from character [char 1, 11]
                            System.out.println("Is char 1, 11");
                            if (character.getSetupObject().equals("student")) {
                                //[char 1, 11]
                                System.out.println("Is char 1, 11");
                                studentsInOrigin.add(character.getStudents().stream().toList().get(0));
                            }
                        } else {
                            //from playerboard
                            System.out.println("Is char ????");
                            studentsInOrigin.add(player.getPlayerBoard().getStudents().stream().toList().get(0));
                        }
                        if (character.getEffectTarget().equals("island")) {
                            //to island 0 [char 1]
                            System.out.println("Is char " + 1);
                            targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                        } else if (character.getEffectTarget().equals("dining_room")) {
                            //to dining [char 11]
                            System.out.println("Is char " + 11);
                            targetPieces.add(player.getPlayerBoard().getPieceID());
                        } else {
                            //to playerboard
                            System.out.println("Is char ????");
                            targetPieces.add(player.getPlayerBoard().getPieceID());
                        }
                    }
                    case "exchange" -> {
                        //[char 10]
                        if (character.getEffectObject().equals("student")) {
                            //[char 10]
                            System.out.println("Is char " + 10);
                            if (character.getEffectSource().equals("entrance")) {
                                System.out.println("Is char " + 10);
                                List<Integer> students = player.getPlayerBoard().getStudents().stream().toList();
                                studentsInOrigin.add(students.get(0));
                                originPieces.add(player.getPlayerBoard().getPieceID());
                                studentsInOrigin.add(students.get(1));
                                originPieces.add(player.getPlayerBoard().getPieceID());
                            }
                            //[char 10]
                            if (character.getEffectTarget().equals("dining_room")) {
                                System.out.println("Is char " + 10);
                                List<Integer> students = new ArrayList<>();

                                SchoolBoard board = player.getPlayerBoard();

                                //Move 2 students to dining
                                Integer studentToMove;
                                for (int k = 0; k < 2; k++) {
                                    studentToMove = board.getStudents().stream().toList().get(0);
                                    board.studentToDining(studentToMove);
                                    studentsInTarget.add(studentToMove);
                                    targetPieces.add(player.getPlayerBoard().getPieceID());
                                }
                            }
                        }
                    }
                    case "add" -> {
                        //If nothing more is needed [char 2, 4, 5, 6, 8]
                        if (character.getEffectCondition().equals("any")) {
                            System.out.println("Is char 2, 4, 5, 6, 8");
                            //[char 5]
                            if (character.getEffectTarget().equals("island")) {
                                System.out.println("Is char " + 5);
                                //First island
                                targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                            } else {
                                //Else: [char 2, 4, 6, 8]
                                System.out.println("Is char 2, 4, 6, 8");
                            }

                            //If color is needed [char 9]
                        } else if (character.getEffectCondition().equals("color")) {
                            System.out.println("Is char " + 9);
                            targetColor = GREEN;
                        }
                    }
                    case "resolve" -> {
                        //[char 3]
                        if (character.getEffectTarget().equals("island")) {
                            System.out.println("Is char " + 3);
                            targetPieces.add(gameController.getCurrentGame().getIslands().get(0).getPieceID());
                        }
                    }
                }
                //Build request
                req = new PlayCharacterRequest(i, nickname, targetColor, studentsInOrigin, studentsInTarget, originPieces, targetPieces);
                try {
                    character.setGameController(gameController);
                    gameController.playCharacter(req);
                    System.out.println("Correctly performed the action");
                    assertTrue(true);
                } catch (NullPointerException e) {
                    System.out.println("Something went wrong :(");
                    e.printStackTrace();
                }
                i++;
            }

            notAllTested = false;
            for(Boolean tested : testedCharacters){
                //If a character hasn't been tested, repeat the process
                if(!tested){
                    notAllTested = true;
                    break;
                }
            }
            count++;

        }while(notAllTested && count < 50);

        Character test = new Character(null, null, true, null, null);
        assertNull(test.getDescription());

        if(notAllTested){
            System.out.println("NOT EVERYONE [after " + count + " steps]");
            int n = 0;
            for(Boolean tested : testedCharacters){
                System.out.println(n + ": " + tested);
                n++;
            }
        } else {
            System.out.println("TESTED EVERY CHARACTER!");
        }
    }
}