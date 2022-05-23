package it.polimi.ingsw.client.gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SetupController {

    @FXML
    private Button button2p, button3p, button4p;

    public void setPlayerNumber(){

        button2p.setOnAction(event -> {
            System.out.println("2p");

        });
        button3p.setOnAction(event -> {
            System.out.println("3p");

        });
        button4p.setOnAction(event -> {
            System.out.println("4p");

        });

        if(button2p.isPressed()){
            System.out.println("2p");
        }else if(button3p.isPressed()){
            System.out.println("3p");
        }else if(button4p.isPressed()){
            System.out.println("4p");
        }

    }

}