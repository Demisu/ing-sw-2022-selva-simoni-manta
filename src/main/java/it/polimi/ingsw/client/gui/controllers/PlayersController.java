package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

/**
 * controller for the number of players selection GUI scene
 */
public class PlayersController implements GUIController {
    private GUI gui;

    @FXML
    private Button button2p, button3p, button4p;

    @FXML
    private CheckBox expertMode;

    public void onLoad(){
        button2p.setOnAction(e -> {
            setPlayerNumber(2);
            gui.changeScene(GUI.LOBBY);
            gui.getControllerFromName(GUI.LOBBY).onLoad();
        });
        button3p.setOnAction(e -> {
            setPlayerNumber(3);
            gui.changeScene(GUI.LOBBY);
            gui.getControllerFromName(GUI.LOBBY).onLoad();
        });
        button4p.setOnAction(e -> {
            setPlayerNumber(4);
            gui.changeScene(GUI.LOBBY);
            gui.getControllerFromName(GUI.LOBBY).onLoad();
        });
    }

    /**
     * @param number number of players that will be allowed in the game
     */
    public void setPlayerNumber(int number){
        Platform.runLater(() -> {
            gui.getClientController().setPlayerNumber(number, expertMode.isSelected());
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}