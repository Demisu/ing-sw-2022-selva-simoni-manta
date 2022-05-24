package it.polimi.ingsw.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class PlayerNumberController {

    @FXML
    private Button button2p, button3p, button4p, buttonConfirmNick;

    @FXML
    private CheckBox checkBoxExpertMode;

    public void setPlayerNumber(){

        button2p.setOnAction(event -> {
            System.out.println("2p");
            setPlayerNumber(2);
        });

        button3p.setOnAction(event -> {
            System.out.println("3p");
            setPlayerNumber(3);
        });

        button4p.setOnAction(event -> {
            System.out.println("4p");
            setPlayerNumber(4);
        });
    }

    public void setPlayerNumber(int number){
        Boolean expertMode = checkBoxExpertMode.isSelected();
        System.out.println("Selected: " +  number);
        System.out.println("Expert mode is: " +  (expertMode ? "on" : "off"));
    }

}