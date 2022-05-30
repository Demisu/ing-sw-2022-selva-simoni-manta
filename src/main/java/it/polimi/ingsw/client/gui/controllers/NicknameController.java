package it.polimi.ingsw.client.gui.controllers;

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

public class NicknameController {

    private Stage stage;
    private Scene scene;

    @FXML
    private Button buttonConfirmNick;

    @FXML
    private TextField textFieldNickname;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/realm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setNickname() {

        buttonConfirmNick.setOnAction(event -> {
            System.out.println("confirmed");
            String nickname = textFieldNickname.getText();
            setNickname(nickname);
        });
    }

    public void setNickname(String nickname){
        System.out.println("Chosen:  " +  nickname);
    }
}