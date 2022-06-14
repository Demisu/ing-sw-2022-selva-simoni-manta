package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Character;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CharactersController implements GUIController {


    private final Image coinImage = new Image(getClass().getResourceAsStream("/assets/coin.png"));
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button;

    @FXML
    private ImageView character1, character2, character3, coin1, coin2, coin3;
    private ArrayList<ImageView> guiCharacter, coins;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene("realm.fxml");
    }

    public void onRun() {

         guiCharacter = new ArrayList<>(){
            {
                add(character1);
                add(character2);
                add(character3);
            }
         };
        coins = new ArrayList<>(){
            {
                add(coin1);
                add(coin2);
                add(coin3);
            }
        };

        //All face down
        guiCharacter.forEach(character ->
                character.setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg"))));

        Platform.runLater(() -> {

            ArrayList<Character> characters = gui.getClientController().getCharacters();
            for (Character character : characters){
                //Available character face up
                if(!character.getHasBeenUsed()){
                    int index = characters.indexOf(character);
                    guiCharacter.get(index)
                            .setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg")));
                    guiCharacter.get(index)
                            .setOnMouseClicked(mouseEvent -> {
                                //Play it
                                Platform.runLater(() -> {
                                    if(gui.getClientController().playCharacter(index)){
                                        //Turn the card face down
                                        guiCharacter.get(index)
                                                .setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                                    } else {
                                        System.out.println("No");
                                    }
                                });
                            });
                    if (character.getHasIncreasedCost()) {
                        coins.get(index).setImage(coinImage);
                    }
                }
            }
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}
