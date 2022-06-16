package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CloudController implements GUIController {
    private Stage stage;
    private GUI gui;

    @FXML
    private Button realm;

    public void onLoad(){
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
