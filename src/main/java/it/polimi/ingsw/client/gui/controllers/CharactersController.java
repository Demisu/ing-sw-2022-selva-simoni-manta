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
    private Button realm;
    @FXML
    private Button info1, info2, info3;

    @FXML
    private ImageView character1, character2, character3, coin1, coin2, coin3;

    @FXML
    private Pane content1, content2, content3;

    private ArrayList<Button> info;
    private ArrayList<ImageView> guiCharacter, coins;
    private ArrayList<Pane> contents;

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
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });

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
        if(!character.getHasBeenUsed()){
            guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg")));
            guiCharacter.get(index).setOnMouseClicked(mouseEvent -> {
                //Play it
                Platform.runLater(() -> {
                    if(gui.getClientController().playCharacter(index)){
                        //Turn the card face down
                        guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                    } else {
                        System.out.println("No");
                    }
                });
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
                colorImage.setImage(new Image(getClass().getResourceAsStream("/assets/noEntry.png")));
                studentWrapper.getChildren().add(colorImage);

                //Image
                colorImage.setLayoutX(counterX);
                colorImage.setFitHeight(studentSize);
                colorImage.setFitWidth(studentSize);
            }

            //Setup next
            counterX += studentSize;
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}
