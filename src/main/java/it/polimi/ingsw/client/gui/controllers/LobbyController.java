package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LobbyController implements GUIController {
    private GUI gui;

    @FXML
    private Button refresh;

    public void onLoad(){
        refresh.setOnAction(e -> Platform.runLater(() -> {
            if(gui.getClientController().isGameStarted()){
                gui.getClientController().getModelInfo();
                if(!gui.getClientController().getGameInfo().isExpertMode()){
                    ((RealmController) gui.getControllerFromName(GUI.REALM)).noExpertMode();
                }
                gui.changeScene(GUI.REALM);
            }
        }));
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
