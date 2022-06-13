package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
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

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene("realm.fxml");
    }

    /**
     * Plays the selected character. Note the character to call is uniquely identified by the AccessibleText that was
     * set when randomly chosen during game setup.
     * @param event
     */
    public void onCharacter(MouseEvent event) {
        Platform.runLater(() -> {
            character1.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playCharacter(1);
            });
            character2.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playCharacter(2);
            });
            character3.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playCharacter(3);
            });

        });
    }

    public void onRun() {
        Platform.runLater(() -> {
            ArrayList<Character> characters = gui.getClientController().getCharacters();
            int count=0;
            for (Character character : characters) {
                if(!character.getHasBeenUsed()) {
                    switch (count) {
                        case 0 -> {
                            Image image = new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg"));
                            character1.setImage(image);
                            if (character.getHasIncreasedCost()) {
                                coin1.setImage(coinImage);
                            }
                        }
                        case 1 -> {
                            Image image = new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg"));
                            character2.setImage(image);
                            if (character.getHasIncreasedCost()) {
                                coin2.setImage(coinImage);
                            }
                        }
                        case 2 -> {
                            Image image = new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg"));
                            character3.setImage(image);
                            if (character.getHasIncreasedCost()) {
                                coin3.setImage(coinImage);
                            }
                        }
                    }
                }
                count++;
            }

        });
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}
