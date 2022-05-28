package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class GUI extends Application implements ClientView {

    private final ClientController clientController;
    private Parent root;

    public static void main(String[] args) throws IOException  {
        launch(args);
    }

    public GUI(){
        this.clientController = null;
    }

    public GUI(ClientController clientController){
        this.clientController = clientController;
        try {
            main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeRoot(String name) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/" + name + ".fxml"));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.changeRoot("realm");
        //this.changeRoot("players");
        //this.changeRoot("action");
        primaryStage.setTitle("Eriantys");
        primaryStage.setMaximized(false);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setMinWidth(1280);
        primaryStage.setMaxWidth(1280);
        primaryStage.show();
    }

    @Override
    public void waitGameStartPhase() {

    }

    @Override
    public void setupPhase() {

    }

    @Override
    public void testingPhase() {

    }

    @Override
    public void planningPhase() {

    }

    @Override
    public void actionPhase() {

    }
}