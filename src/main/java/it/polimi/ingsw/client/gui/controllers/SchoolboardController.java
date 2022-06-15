package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SchoolboardController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.REALM);
    }

    public void drawSchoolBoard(Player player){
        System.out.println("TO BE IMPLEMENTED");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
