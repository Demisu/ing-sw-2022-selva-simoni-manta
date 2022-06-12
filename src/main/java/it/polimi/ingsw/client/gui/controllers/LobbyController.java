package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.GamePhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyController implements GUIController {
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        Platform.runLater(() -> {
            if(gui.getClientController().isGameStarted()){
                gui.changeScene("realm.fxml");
            }
        });
        /*Parent root = FXMLLoader.load(getClass().getResource("/realm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
