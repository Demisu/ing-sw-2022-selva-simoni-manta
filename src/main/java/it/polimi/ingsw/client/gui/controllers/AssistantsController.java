package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AssistantsController implements GUIController {
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button;

    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4, assistant5,
                        assistant6, assistant7, assistant8, assistant9, assistant10;


    public void switchToRealmScene(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/realm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public int getIndexOfAssistant(int priority, int movement) {
        List<Assistant> assistants = gui.getClientController().getPlayerInfo().getDeck();
        for(Assistant assistant : assistants) {
            if (assistant.getTurnPriority() == priority && assistant.getMotherNatureMovements() == movement) {
                return assistants.indexOf(assistant);
            }
        }
        return 0;
    }

    public void onAssistant() {
        assistant1.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(1,1));
        });

        assistant2.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(2,1));
        });

        assistant3.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(3,2));

        });

        assistant4.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(4,2));

        });

        assistant5.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(5,3));

        });

        assistant6.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(6,3));

        });

        assistant7.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(7,4));

        });

        assistant8.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(8,4));

        });

        assistant9.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(9,5));

        });

        assistant10.setOnMouseClicked(mouseEvent -> {
            gui.getClientController().playAssistant(getIndexOfAssistant(10,5));

        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
