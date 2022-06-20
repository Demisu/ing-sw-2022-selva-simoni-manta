package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartController implements GUIController {
    private GUI gui;
    private boolean muted;

    @FXML
    private Button sound;

    public void switchToNicknameScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.NICKNAME);
        gui.getControllerFromName(GUI.NICKNAME).onLoad();
    }

    public void closeApplication(ActionEvent e) throws IOException {
        Platform.exit();
    }

    public void switchVolume(ActionEvent e) throws IOException {
        if(muted){// 42 39
            gui.getMusicPlayer().play();
            sound.setText("\uD83D\uDD0A");
            sound.setMaxHeight(39);
            sound.setMinHeight(39);
            sound.setMaxWidth(42);
            sound.setMinWidth(42);
            muted = false;
        }else{
            gui.getMusicPlayer().stop();
            sound.setText("\uD83D\uDD08");
            sound.setMaxHeight(39);
            sound.setMinHeight(39);
            sound.setMaxWidth(42);
            sound.setMinWidth(42);
            muted = true;
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void onLoad() {

    }
}
