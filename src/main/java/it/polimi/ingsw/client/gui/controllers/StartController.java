package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController implements GuiController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button volumeButton;

    public void switchToNicknameScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/nickname.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeApplication(ActionEvent e) throws IOException {
        Platform.exit();
    }

    public void muteVolume(ActionEvent e) throws IOException {
        volumeButton.setOnAction(event -> {
        });
    }

    public void alignButton(ActionEvent e) throws IOException {
        volumeButton.setAlignment(Pos.BOTTOM_RIGHT);
        System.out.println("ciao");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
