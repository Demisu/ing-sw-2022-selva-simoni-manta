package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * controller of the assistants scene
 */
public class AssistantsController implements GUIController {
    private GUI gui;

    @FXML
    private Button realm, gameStatus;

    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4, assistant5, assistant6, assistant7, assistant8, assistant9, assistant10;

    private ArrayList<ImageView> guiAssistants;

    public void onLoad() {
        ColorAdjust darken = new ColorAdjust();
        darken.setBrightness(-0.6);
        ColorAdjust resetBrightness = new ColorAdjust();
        resetBrightness.setBrightness(0);

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

        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            gui.getControllerFromName(GUI.PROFILES).onLoad();
        });

        Platform.runLater(() -> {
            List<Assistant> deck = gui.getClientController().getPlayerInfo().getDeck();
            int playerId = gui.getClientController().getPlayerInfo().getPlayerId() + 1;
            //All face down
            guiAssistants.forEach(assistant ->
                    assistant.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + playerId + ".jpg"))));
            //Set default cursor
            guiAssistants.forEach(assistant -> assistant.setCursor(Cursor.DEFAULT));

            //Check available assistants
            ArrayList<Integer> playableAssistantIDs = new ArrayList<>();
            for(Assistant assistantTemp : gui.getClientController().getPlayerInfo().getDeck()){
                playableAssistantIDs.add(assistantTemp.getAssistantId());
            }
            for (Player player : gui.getClientController().getGameInfo().getPlayers()){
                if(player.getLastAssistantPlayed() != null && playableAssistantIDs.contains(player.getLastAssistantPlayed().getAssistantId())) {
                    playableAssistantIDs.remove((Integer) (player.getLastAssistantPlayed().getAssistantId()));
                }
            }

            for (Assistant assistant : deck){
                //Available assistant face up
                ImageView assistantToRender = guiAssistants.get(assistant.getAssistantId() - 1);
                assistantToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (" + assistant.getAssistantId() + ").png")));
                //Darken already used assistants
                if (!playableAssistantIDs.contains(assistant.getAssistantId())){
                    assistantToRender.setEffect(darken);
                } else {
                    assistantToRender.setEffect(resetBrightness);
                    assistantToRender.setEffect(new DropShadow());
                    //Set hand cursor
                    assistantToRender.setCursor(Cursor.HAND);
                    //Available assistant setOnClick
                    assistantToRender.setOnMouseClicked(mouseEvent -> {
                        //Play it
                        Platform.runLater(() -> {
                            if (gui.getClientController().playAssistant(getIndexOfAssistant(assistant.getTurnPriority(), assistant.getMotherNatureMovements()))) {
                                //Turn the card face down
                                assistantToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + playerId + ".jpg")));
                                //Set default cursor
                                assistantToRender.setCursor(Cursor.DEFAULT);
                                gui.getClientController().getModelInfo();
                                gui.changeScene(GUI.PROFILES);
                                gui.getControllerFromName(GUI.PROFILES).onLoad();
                            } else {
                                //Might want to add a popup here with an error
                                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot play assistant", ButtonType.OK);
                                alert.showAndWait();
                            }
                        });
                    });
                }
            }
        });
    }

    /**
     * @param priority priority number
     * @param movement movement number
     * @return index of the assistant
     */
    public int getIndexOfAssistant(int priority, int movement) {
        List<Assistant> assistants = gui.getClientController().getPlayerInfo().getDeck();
        for(Assistant assistant : assistants) {
            if (assistant.getTurnPriority() == priority && assistant.getMotherNatureMovements() == movement) {
                return assistants.indexOf(assistant);
            }
        }
        return 0;
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
