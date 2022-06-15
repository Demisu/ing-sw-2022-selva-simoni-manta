package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
                        ((LobbyController) gui.getControllerFromName(GUI.LOBBY)).onLoad();
                    }
                    case 1 -> {
                        gui.changeScene(GUI.PLAYERS);
                        ((PlayersController) gui.getControllerFromName(GUI.PLAYERS)).onLoad();
                    }
                    case 2 -> {
                        Platform.runLater(() -> {
                            gui.getClientController().closeConnection();
                        });
                        Platform.exit();
                    }
                }
            }else{
                System.out.println("Inserisci un nickname!"); //Might want to add a popup displaying the message
            }
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}