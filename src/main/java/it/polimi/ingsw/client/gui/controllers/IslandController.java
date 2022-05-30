package it.polimi.ingsw.client.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class IslandController {
    private Stage stage;
    private Scene scene;

    @FXML
    private Button button;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/realm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
