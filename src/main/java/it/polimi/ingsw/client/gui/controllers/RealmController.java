package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
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

public class RealmController implements GUIController {

    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button1, button2, button3, button4, button5, charsButton, menuButton;

    @FXML
    private ImageView island;

    public void switchToSchoolBoardScene(ActionEvent e) throws IOException {
        gui.changeScene("schoolboard.fxml");
    }

    public void switchToGameSetupScene(ActionEvent e) throws IOException {
        gui.changeScene("start.fxml");
    }

    public void switchToAssistantsScene(ActionEvent e) throws IOException {
        gui.changeScene("assistants.fxml");
        ((AssistantsController) gui.getControllerFromName("assistants.fxml")).onRun();
    }

    public void switchToCharactersScene(ActionEvent e) throws IOException {
        gui.changeScene("characters.fxml");
        ((CharactersController) gui.getControllerFromName("characters.fxml")).onRun();
    }

    public void switchToIsland1Scene(MouseEvent e) throws IOException {
        gui.changeScene("island1.fxml");
    }

    public void switchToCloudScene(MouseEvent e) throws IOException {
        gui.changeScene("cloud.fxml");
    }

    public void switchToIsland2Scene(MouseEvent e) throws IOException {
        gui.changeScene("island2.fxml");
    }

    public void switchToIsland3Scene(MouseEvent e) throws IOException {
        gui.changeScene("island3.fxml");
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

    public void noExpertMode(){
        charsButton.setVisible(false);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
