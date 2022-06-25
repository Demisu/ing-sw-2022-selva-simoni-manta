package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;

/**
 * controller for the game status GUI scene
 */
public class ProfilesController implements GUIController {
    private GUI gui;
    List<Team> teams;

    @FXML
    private ImageView playerImage1, playerImage2, playerImage3, playerImage4;
    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4;

    @FXML
    private Text roundDisplay, phaseDisplay, currentPlayerDisplay;
    @FXML
    private Text nickname1, nickname2, nickname3, nickname4;
    @FXML
    private Text team1, team2, team3, team4;
    @FXML
    private Text emoji1, emoji2, emoji3, emoji4;
    @FXML
    private Text coins1, coins2, coins3, coins4;
    @FXML
    private Text clickToPlay1, clickToPlay2, clickToPlay3, clickToPlay4;

    @FXML
    private Button showSchoolboard1, showSchoolboard2, showSchoolboard3, showSchoolboard4;
    @FXML
    private Button realm;

    private List<ImageView> guiPlayers;
    private List<Text> guiNicknames;
    private List<Text> guiTeams;
    private List<Text> guiEmojis;
    private List<Text> guiCoins;
    private List<Text> guiClickToPlay;
    private List<ImageView> guiAssistants;
    private List<Button> guiShowSchoolBoards;

    public void onLoad() {
        guiPlayers = new ArrayList<>(){
            {
                add(playerImage1);
                add(playerImage2);
                add(playerImage3);
                add(playerImage4);
            }
        };
        guiNicknames = new ArrayList<>(){
            {
                add(nickname1);
                add(nickname2);
                add(nickname3);
                add(nickname4);
            }
        };
        guiTeams = new ArrayList<>(){
            {
                add(team1);
                add(team2);
                add(team3);
                add(team4);
            }
        };
        guiEmojis = new ArrayList<>(){
            {
                add(emoji1);
                add(emoji2);
                add(emoji3);
                add(emoji4);
            }
        };
        guiCoins = new ArrayList<>(){
            {
                add(coins1);
                add(coins2);
                add(coins3);
                add(coins4);
            }
        };
        guiClickToPlay = new ArrayList<>(){
            {
                add(clickToPlay1);
                add(clickToPlay2);
                add(clickToPlay3);
                add(clickToPlay4);
            }
        };
        guiAssistants = new ArrayList<>(){
            {
                add(assistant1);
                add(assistant2);
                add(assistant3);
                add(assistant4);
            }
        };
        guiShowSchoolBoards = new ArrayList<>(){
            {
                add(showSchoolboard1);
                add(showSchoolboard2);
                add(showSchoolboard3);
                add(showSchoolboard4);
            }
        };
        guiShowSchoolBoards.forEach(btn -> btn.setVisible(false));
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
        Platform.runLater(() -> {
            Game currentGame = gui.getClientController().getGameInfo();
            roundDisplay.setText("Round " + (currentGame.getTurnNumber() + 1));
            phaseDisplay.setText("Phase: " + currentGame.getCurrentPhase());
            if(currentGame.getCurrentPhase().equals(GamePhase.END)) {
                Team winnerTeam = currentGame.getWinnerTeam();
                String players = "";
                for(Player player : winnerTeam.getPlayers()){
                    players += player.getNickname() + " ";
                }
                currentPlayerDisplay.setText("Winner team: " + (winnerTeam.getTeamId() + 1) + "\n[ " + players + "]");
            } else {
                currentPlayerDisplay.setText("Current Player: " + currentGame.getCurrentPlayer());
            }
            List<Player> players = currentGame.getPlayers();
            teams = currentGame.getTeams();
            players.forEach(this::drawPlayer);
        });
    }

    /**
     * @param player player that needs data to be shown
     */
    public void drawPlayer(Player player){
        ColorAdjust darken = new ColorAdjust();
        darken.setBrightness(-0.6);

        int id = player.getPlayerId();
        boolean sameAsClient = player.getNickname().equals(gui.getClientController().getPlayerInfo().getNickname());
        ImageView playerToRender = guiPlayers.get(id);
        ImageView assistantToRender = guiAssistants.get(id);

        //Player Image
        playerToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + (id + 1) + ".jpg")));
        //Add inner shadow
        playerToRender.setEffect(new InnerShadow());

        //Assistant played
        if(player.getLastAssistantPlayed() != null){
            //Jolly assistant, used when the player is disconnected
            if(player.getLastAssistantPlayed().getAssistantId() == -1){
                //Show generic back
                assistantToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                guiClickToPlay.get(id).setText("AFK");
                guiAssistants.get(id).setEffect(darken);
            } else {
                //Played assistant, show it
                assistantToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (" + player.getLastAssistantPlayed().getAssistantId() + ").png")));
                assistantToRender.setCursor(Cursor.DEFAULT);
                assistantToRender.setEffect(new DropShadow());
                //Remove hint text
                guiClickToPlay.get(id).setText("");
            }
        } else {
            //Still need to play assistant, show generic back
            assistantToRender.setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
            //If this is the current player, he can click on it to play an assistant
            if(sameAsClient) {
                assistantToRender.setOnMouseClicked(e -> {
                    gui.changeScene(GUI.ASSISTANTS);
                    gui.getControllerFromName(GUI.ASSISTANTS).onLoad();
                });
                assistantToRender.setCursor(Cursor.HAND);
                //Add hint text, if this is the client player
                guiClickToPlay.get(id).setText("Click to play");
                //Clickable hint, to avoid unresponsive parts
                guiClickToPlay.get(id).setOnMouseClicked(e -> {
                    gui.changeScene(GUI.ASSISTANTS);
                    gui.getControllerFromName(GUI.ASSISTANTS).onLoad();
                });
                guiClickToPlay.get(id).setCursor(Cursor.HAND);
            }
        }
        //Add outer shadow to assistant, if the player is not AFK
        if(assistantToRender.getEffect() == null) {
            assistantToRender.setEffect(new DropShadow());
        }

        //Nickname
        if(player.isActive()){
            guiNicknames.get(id).setText(player.getNickname() + (sameAsClient ? " [YOU]" : ""));
        } else {
            guiNicknames.get(id).setText(player.getNickname() + "[AFK]");
            guiPlayers.get(id).setEffect(darken);
        }

        //Team
        guiTeams.get(id).setText("Team "+(player.getTeamID() + 1));

        //Coins
        guiEmojis.get(id).setText("💰");
        guiCoins.get(id).setText(player.getCoins().toString());

        //School board button
        guiShowSchoolBoards.get(id).setVisible(true);
        guiShowSchoolBoards.get(id).setOnAction(e -> {
            gui.changeScene(GUI.SCHOOLBOARD);
            gui.getControllerFromName(GUI.SCHOOLBOARD).onLoad();
            ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).drawSchoolBoard(player);
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
