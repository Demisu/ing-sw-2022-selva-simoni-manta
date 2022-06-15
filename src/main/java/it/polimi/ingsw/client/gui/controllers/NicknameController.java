package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.client.requests.GetUpdatedBoardRequest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NicknameController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button buttonConfirmNick;

    @FXML
    private TextField textFieldNickname;

    public void switchToPlayersScene(ActionEvent e) throws IOException {
        int result;
        if(!textFieldNickname.getText().equals("")){
            result = gui.getClientController().setPlayerNickname(textFieldNickname.getText());
            switch(result){
                case 0 -> {
                    gui.changeScene(GUI.LOBBY);
                }
                case 1 -> {
                    gui.changeScene(GUI.PLAYERS);
                }
                case 2 -> {
                    Platform.runLater(() -> {
                        gui.getClientController().closeConnection();
                    });
                    Platform.exit();
                }
            }
        }else{
            System.out.println("Inserisci un nickname!");
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}