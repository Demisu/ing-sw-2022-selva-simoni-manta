package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
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

public class NicknameController implements GuiController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button buttonConfirmNick;

    @FXML
    private TextField textFieldNickname;

    public void switchToPlayersScene(ActionEvent e) throws IOException {
        System.out.println(textFieldNickname.getText());

        Parent root = FXMLLoader.load(getClass().getResource("/players.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}