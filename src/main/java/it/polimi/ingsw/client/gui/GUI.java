package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        this.changeRoot("start"); // start  -  players  -  action  -  realm
        primaryStage.setTitle("Eriantys");
        primaryStage.setMaximized(false);
        primaryStage.setMinWidth(1295);
        //primaryStage.setMaxWidth(1295);
        primaryStage.setMinHeight(758);
        //primaryStage.setMaxHeight(758);
        primaryStage.setScene(new Scene(root, 1280, 720));
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