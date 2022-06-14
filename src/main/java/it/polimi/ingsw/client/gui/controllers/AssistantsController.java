package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Character;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
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
    private ArrayList<ImageView> guiAssistants;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene("realm.fxml");
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

    public void onRun() {
        Platform.runLater(() -> {
            List<Assistant> deck = gui.getClientController().getPlayerInfo().getDeck();
            int playerId = gui.getClientController().getPlayerInfo().getPlayerId() + 1;

            guiAssistants = new ArrayList<>(){
                {
                    add(assistant1);
                    add(assistant2);
                    add(assistant3);
                    add(assistant4);
                    add(assistant5);
                    add(assistant6);
                    add(assistant7);
                    add(assistant8);
                    add(assistant9);
                    add(assistant10);
                }
            };

            //All face down
            guiAssistants.forEach(assistant ->
                    assistant.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + playerId + ".jpg"))));

            for (Assistant assistant : deck){
                //Available assistant face up
                guiAssistants.get(assistant.getAssistantId() - 1)
                        .setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (" + assistant.getAssistantId() + ").png")));
                //Available assistant setOnClick
                guiAssistants.get(assistant.getAssistantId() - 1).setOnMouseClicked(mouseEvent -> {
                    //Play it
                    Platform.runLater(() -> {
                        if(gui.getClientController().playAssistant(getIndexOfAssistant(assistant.getTurnPriority(),assistant.getMotherNatureMovements()))){
                            //Turn the card face down
                            guiAssistants.get(assistant.getAssistantId() - 1)
                                    .setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + playerId + ".jpg")));
                        } else {
                            System.out.println("No");
                        }
                    });
                });
            }
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
