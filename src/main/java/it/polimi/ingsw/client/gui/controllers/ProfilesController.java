package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.GamePhase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProfilesController implements GUIController {
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private ImageView playerImage1, playerImage2, playerImage3, playerImage4;

    @FXML
    private Text nickname1, nickname2, nickname3, nickname4;

    @FXML
    private Text team1, team2, team3, team4;

    @FXML
    private Text emoji1, emoji2, emoji3, emoji4;

    @FXML
    private Text coins1, coins2, coins3, coins4;

    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4;

    @FXML
    private Button showSchoolboard1, showSchoolboard2, showSchoolboard3, showSchoolboard4;

    @FXML
    private Button realm;

    public void onRun() {
        realm.setOnMouseClicked(e -> {
            gui.changeScene("realm.fxml");
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
