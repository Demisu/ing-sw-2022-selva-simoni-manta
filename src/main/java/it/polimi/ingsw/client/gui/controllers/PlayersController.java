package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayersController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button2p, button3p, button4p;

    @FXML
    private CheckBox checkBoxExpertMode;

    public void twoPlayers(ActionEvent e) throws IOException {
        setPlayerNumber(2);
        switchToRealmScene(e);
    }

    public void treePlayers(ActionEvent e) throws IOException {
        setPlayerNumber(3);
        switchToRealmScene(e);
    }

    public void fourPlayers(ActionEvent e) throws IOException {
        setPlayerNumber(4);
        switchToRealmScene(e);
    }

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.LOBBY);
    }

    public void setPlayerNumber(int number){
        Platform.runLater(() -> {
            gui.getClientController().setPlayerNumber(number, checkBoxExpertMode.isSelected());
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}