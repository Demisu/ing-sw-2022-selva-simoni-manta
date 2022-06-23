package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

import java.util.List;

public class LobbyController implements GUIController {
    private GUI gui;

    @FXML
    private Text message;

    public void onLoad(){

        if(gui.getClientController().getGameInfo() != null && !gui.getClientController().getGamePhase().equals(GamePhase.SETUP)){
            if(!gui.getClientController().getGameInfo().isExpertMode()){
                ((RealmController) gui.getControllerFromName(GUI.REALM)).expertMode = false;
            }
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        } else if(gui.getClientController().getGameInfo() != null && gui.getClientController().getGamePhase().equals(GamePhase.SETUP)) {
            StringBuilder playersNames = new StringBuilder();
            List<Player> players = gui.getClientController().getGameInfo().getPlayers();
            for(int i = 0; i < gui.getClientController().getGameInfo().connectedPlayersNumber() + 1; i++){
                playersNames.append("[").append(players.get(i).getNickname()).append("]").append(" ");
            }
            message.setText("Players connected: "
                    + (gui.getClientController().getGameInfo().connectedPlayersNumber() + 1)
                    + "/"
                    + gui.getClientController().getGameInfo().getPlayers().size()
                    + "\n"
                    + playersNames);
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
