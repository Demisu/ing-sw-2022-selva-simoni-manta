package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Island;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RealmController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button1, button2, button3, button4, button5, charsButton, menuButton, profiles;

    @FXML
    private ImageView island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12, cloud1, cloud2, cloud3, cloud4;
    private ArrayList<ImageView> guiIslands;

    public void switchToSchoolBoardScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.SCHOOLBOARD);
    }

    public void switchToGameSetupScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.MENU);
    }

    public void switchToAssistantsScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.ASSISTANTS);
        ((AssistantsController) gui.getControllerFromName(GUI.ASSISTANTS)).onRun();
    }

    public void switchToCharactersScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.CHARACTERS);
        ((CharactersController) gui.getControllerFromName(GUI.CHARACTERS)).onRun();
    }

    public void switchToProfiles(ActionEvent e) throws IOException {
        gui.changeScene(GUI.PROFILES);
        ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onRun();
    }

    public void switchToCloudScene(MouseEvent e) throws IOException {
        gui.changeScene(GUI.CLOUD);
    }

    public void switchToIsland1Scene(MouseEvent e) throws IOException {
        gui.changeScene(GUI.ISLAND1);
    }

    public void switchToIsland2Scene(MouseEvent e) throws IOException {
        gui.changeScene(GUI.ISLAND2);
    }

    public void switchToIsland3Scene(MouseEvent e) throws IOException {
        gui.changeScene(GUI.ISLAND3);
    }

    public void openBag(MouseEvent mouseEvent) throws IOException {
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        final Stage dialog = new Stage();
        Pane bagRoot = new Pane();
        StackPane bagHolder = new StackPane();
        Canvas canvas = new Canvas(2000,2000);
        bagHolder.getChildren().add(canvas);
        bagRoot.getChildren().add(bagHolder);
        Scene bagScene = new Scene(bagRoot, 600, 400);
        dialog.setScene(bagScene);
        dialog.initModality(Modality.NONE);
        dialog.initOwner(stage);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/assets/bag.png")));
        VBox dialogVbox = new VBox(20);
        dialogVbox.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, CornerRadii.EMPTY, Insets.EMPTY)));
        dialogVbox.getChildren().add(new Text("\t [INFO TO BE ADDED..]"));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void onRun(){
        guiIslands = new ArrayList<>(){
            {
                add(island1);
                add(island2);
                add(island3);
                add(island4);
                add(island5);
                add(island6);
                add(island7);
                add(island8);
                add(island9);
                add(island10);
                add(island11);
                add(island12);
            }
        };
        guiIslands.forEach(island -> island.setVisible(false));
        Platform.runLater(() -> {
            List<Island> islands = gui.getClientController().getIslands();
            islands.forEach(island -> guiIslands.get(island.getPieceID()).setVisible(true));
        });
    }

    public void noExpertMode(){
        charsButton.setVisible(false);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
