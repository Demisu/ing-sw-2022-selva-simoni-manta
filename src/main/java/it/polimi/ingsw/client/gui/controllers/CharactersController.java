package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CharactersController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button;

    @FXML
    private ImageView character1, character2, character3;

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
                gui.getClientController().playCharacter(Integer.parseInt(character1.getAccessibleText()));
            });
            character2.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playCharacter(Integer.parseInt(character2.getAccessibleText()));
            });
            character3.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playCharacter(Integer.parseInt(character3.getAccessibleText()));
            });

        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
