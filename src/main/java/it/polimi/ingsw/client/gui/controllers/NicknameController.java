package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 * controller for the nickname selection GUI scene
 */
public class NicknameController implements GUIController {
    private GUI gui;

    @FXML
    private Button confirm;

    @FXML
    private TextField nickname;

    public void onLoad() {
        confirm.setOnAction(e -> {
            int result;
            String nick = nickname.getText();
            if(!nick.equals("")){
                result = gui.getClientController().setPlayerNickname(nick);
                switch(result){
                    case 0 -> {
                        gui.changeScene(GUI.LOBBY);
                        gui.getControllerFromName(GUI.LOBBY).onLoad();
                    }
                    case 1 -> {
                        gui.changeScene(GUI.PLAYERS);
                        gui.getControllerFromName(GUI.PLAYERS).onLoad();
                    }
                    case 2 -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Game is full! Click ok to quit", ButtonType.OK);
                        alert.showAndWait();
                        if(alert.getResult().equals(ButtonType.OK)){
                            Platform.runLater(() -> {
                                gui.getClientController().closeConnection();
                            });
                        } else {
                            Platform.runLater(() -> {
                                gui.getClientController().closeConnection();
                            });
                        }
                        Platform.exit();
                    }
                }
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nickname is invalid! Please try again", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}