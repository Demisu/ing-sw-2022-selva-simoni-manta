package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.GamePhase;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LobbyController implements GUIController {
    private GUI gui;

    @FXML
    private Button refresh;

    public void onLoad(){

        if(gui.getClientController().getGameInfo() != null && !gui.getClientController().getGamePhase().equals(GamePhase.SETUP)){
            if(!gui.getClientController().getGameInfo().isExpertMode()){
                ((RealmController) gui.getControllerFromName(GUI.REALM)).expertMode = false;
            }
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        }

        refresh.setOnAction(e -> Platform.runLater(() -> {
            if(gui.getClientController().isGameStarted()){
                gui.getClientController().getModelInfo();
                if(!gui.getClientController().getGameInfo().isExpertMode()){
                    ((RealmController) gui.getControllerFromName(GUI.REALM)).expertMode = false;
                }
                gui.changeScene(GUI.REALM);
                ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
            }
        }));
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
