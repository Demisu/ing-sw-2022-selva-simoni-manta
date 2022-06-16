package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SchoolboardController implements GUIController {
    private GUI gui;

    @FXML
    private Button realm, gameStatus;

    public void drawSchoolBoard(Player player){// This schoolBoard is just to show, without functionality
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
        //[TO BE IMPLEMENTED..]
    }
//These two functions might not be enough to reset the old state of the FXML between them!
    public void onLoad(){
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
        });
        //[TO BE IMPLEMENTED..]
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
