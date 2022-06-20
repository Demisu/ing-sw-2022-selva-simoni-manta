package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.StudentAccessiblePiece;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static it.polimi.ingsw.model.GamePhase.ACTION;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static javafx.scene.paint.Color.WHITE;

public class CharactersController implements GUIController {
    private final Image coinImage = new Image(getClass().getResourceAsStream("/assets/coin.png"));
    private Stage stage;
    private GUI gui;

    @FXML
    private Text coinsDisplay;

    @FXML
    private Button realm, undo;
    @FXML
    private Button info1, info2, info3;
    @FXML
    private Button colorBtn, sourceStudentsBtn, sourcePiecesBtn, targetStudentsBtn, targetPiecesBtn;

    @FXML
    private ImageView character1, character2, character3, coin1, coin2, coin3;

    @FXML
    private Pane content1, content2, content3;

    private ArrayList<Button> info;
    private ArrayList<ImageView> guiCharacter, coins;
    private ArrayList<Pane> contents;
    private ArrayList<Button> characterButtons;

    //Variables for positioning
    public final int studentSize = 46;
    public final int textOffsetX = 17;
    public final int textOffsetY = 30;
    int counterX = 0;

    public void onLoad() {
        guiCharacter = new ArrayList<>(){
           {
               add(character1);
               add(character2);
               add(character3);
           }
        };
        contents = new ArrayList<>(){
            {
                add(content1);
                add(content2);
                add(content3);
            }
        };
        info = new ArrayList<>(){
            {
                add(info1);
                add(info2);
                add(info3);
            }
        };
        coins = new ArrayList<>(){
            {
                add(coin1);
                add(coin2);
                add(coin3);
            }
        };
        characterButtons = new ArrayList<>(){
            {
                add(colorBtn);
                add(sourceStudentsBtn);
                add(sourcePiecesBtn);
                add(targetStudentsBtn);
                add(targetPiecesBtn);
            }
        };

        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });

        //Show undo if an action is in progress
        undo.setVisible(!gui.getStatus().equals(GUI.NONE));
        undo.setOnAction(e -> {
            resetStatus();
            gui.changeScene(GUI.CHARACTERS);
            gui.getControllerFromName(GUI.CHARACTERS).onLoad();
        });

        //Hide/Show needed characters buttons
        for(int i = 0; i < characterButtons.size(); i++){
            characterButtons.get(i).setVisible(gui.listOfCharacterButtons().get(i));
            //TODO ADD ONCLICK
        }

        //Remove old images
        contents.forEach(pane -> pane.getChildren().clear());

        coinsDisplay.setText("💰 " + gui.getClientController().getPlayerInfo().getCoins());
        ArrayList<Character> characters = gui.getClientController().getCharacters();
        characters.forEach(this::drawCharacter);
    }

    public void drawCharacter(Character character){

        int index = gui.getClientController().getCharacters().indexOf(character);

        //Info
        info.get(index).setOnAction(e -> {
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            ArrayList<Text> infoChars = new ArrayList<>(){
                {
                    add(new Text("\t [INFO TO BE ADDED..]"));
                }
            };
            gui.createModal(stage, "[CHARACTER NAME TO BE ADDED..]", "coin.png", Color.DARKGRAY, infoChars);
        });

        //Character
        //If it hasn't been used this round and the player is the current turn owner
        if(!character.getHasBeenUsed()
                && gui.getClientController().getGameInfo().getCurrentPlayer().equals(gui.getClientController().getPlayerInfo().getNickname())){

            guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg")));
            guiCharacter.get(index).setOnMouseClicked(mouseEvent -> {
                //If a character was already selected
                if(gui.getStatus().equals(GUI.CHARACTER)){
                    Platform.runLater(() -> {
                        //Play it
                        if(gui.getClientController().playCharacter(gui.getCharacterRequest())) {
                            //Turn the card face down
                            guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                        }
                        resetStatus();
                        gui.changeScene(GUI.CHARACTERS);
                        gui.getControllerFromName(GUI.CHARACTERS).onLoad();
                    });
                } else {
                    //Start building character request
                    gui.setStatus(GUI.CHARACTER);
                    gui.setCharacterIndex(index);

                    //Show buttons needed
                    readCharacterParameters(character);

                    gui.changeScene(GUI.CHARACTERS);
                    gui.getControllerFromName(GUI.CHARACTERS).onLoad();
                }
            });
            if (character.getHasIncreasedCost()) {
                coins.get(index).setImage(coinImage);
            }
        }else{
            guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
        }

        //Content
        switch (character.getSetupObject()) {
            case "student" -> {
                drawStudents(contents.get(index), character);
            }
            case "no_entry" -> {
                drawNoEntry(contents.get(index), character);
            }
        }
    }

    public void drawStudents(Pane studentWrapper, Character character){

        counterX = 0;

        HashSet<Integer> students = character.getStudents();

        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        //Count each color
        students.forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);

        //Draw each color
        for(it.polimi.ingsw.model.Color color : it.polimi.ingsw.model.Color.values()){

            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            ImageView colorImage = new ImageView();
            Text colorNumber = new Text();

            //If there are no students of this color, skip it
            if(tempStudentNumber == 0){
                colorImage.setVisible(false);
                colorNumber.setVisible(false);
            } else {
                colorImage.setImage(new Image(getClass().getResourceAsStream("/assets/coin.png")));
                studentWrapper.getChildren().add(colorImage);

                //Image
                colorImage.setLayoutX(counterX);
                colorImage.setFitHeight(studentSize);
                colorImage.setFitWidth(studentSize);
                colorImage.setEffect(gui.getColorEffect(color));

                //Text
                studentWrapper.getChildren().add(colorNumber);
                colorNumber.setLayoutX(counterX + textOffsetX);
                colorNumber.setLayoutY(textOffsetY);
                colorNumber.setFont(new Font("System Bold", 20.0));
                colorNumber.setFill(WHITE);
                colorNumber.setEffect(new DropShadow());
                colorNumber.setTextAlignment(TextAlignment.CENTER);
                colorNumber.setText(tempStudentNumber.toString());
            }

            //Setup next
            counterX += studentSize;
        }
    }

    public void drawNoEntry(Pane studentWrapper, Character character){

        for(int i = 0, counterX = 0; i < character.getNoEntryNumber(); i++, counterX += studentSize){
            ImageView noEntryImage = new ImageView();
            noEntryImage.setImage(new Image(getClass().getResourceAsStream("/assets/noEntry.png")));
            studentWrapper.getChildren().add(noEntryImage);
            noEntryImage.setLayoutX(counterX);
            noEntryImage.setFitHeight(studentSize);
            noEntryImage.setFitWidth(studentSize);
        }
    }

    public void resetStatus(){

        gui.resetStatus();
        //Hide undo
        undo.setVisible(false);
        gui.reloadScene();
    }

    public void readCharacterParameters(Character character){

        switch (character.getEffectType()) {

            case "move" -> {
                //Source
                if (character.getEffectSource().equals("character")) {
                    //from character [char 1, 11]
                    if (character.getSetupObject().equals("student")) {
                        //[char 1, 11]
                        gui.setSourceStudentsBtnVisible(true);
                    }
                } else if(character.getEffectSource().equals("dining_room")) {
                    //[char 12]
                    gui.setColorBtnVisible(true);
                }

                //Target
                if (character.getEffectTarget().equals("island")) {
                    //[char 1]
                    gui.setTargetPiecesBtnVisible(true);
                } else if (character.getEffectTarget().equals("dining_room")) {
                    //[char 11]
                    gui.setTargetPiecesBtnVisible(true);
                }
            }

            case "exchange" -> {
                if (character.getEffectObject().equals("student")) {
                    gui.setSourceStudentsBtnVisible(true);

                    //[char 10]

                    //Source
                    if (character.getEffectSource().equals("entrance")) {
                        gui.setSourcePiecesBtnVisible(true);
                    } else if(character.getEffectSource().equals("character")) {
                        //[char 7]
                    }

                    //Target
                    if (character.getEffectTarget().equals("dining_room")) {
                        //[char 10]
                        gui.setTargetPiecesBtnVisible(true);
                    } else if(character.getEffectTarget().equals("entrance")) {
                        //[char 7]
                        gui.setTargetPiecesBtnVisible(true);
                    }
                }
            }

            case "add" -> {
                if (character.getEffectCondition().equals("any")) {
                    //If nothing more is needed [char 2, 4, 5, 6, 8]
                    if (character.getEffectTarget().equals("island")) {
                        //[char 5]
                        gui.setTargetPiecesBtnVisible(true);
                    } else {
                        //[char 2, 4, 6, 8]
                        //Nothing more needed
                    }
                } else if (character.getEffectCondition().equals("color")) {
                    //If color is needed [char 9]
                    gui.setColorBtnVisible(true);
                }
            }

            case "resolve" -> {
                if (character.getEffectTarget().equals("island")) {
                    //[char 3]
                    gui.setTargetPiecesBtnVisible(true);
                }
            }
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}
