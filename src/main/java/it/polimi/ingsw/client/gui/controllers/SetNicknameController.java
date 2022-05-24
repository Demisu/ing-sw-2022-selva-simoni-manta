package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SetNicknameController {

    @FXML
    private Button buttonConfirmNick;

    @FXML
    private TextField textFieldNickname;

    public void setNickname() {

        buttonConfirmNick.setOnAction(event -> {
            System.out.println("confirmed");
            String nickname = textFieldNickname.getText();
            setNickname(nickname);
        });
    }

    public void setNickname(String nickname){
        System.out.println("Choosen:  " +  nickname);
    }
}