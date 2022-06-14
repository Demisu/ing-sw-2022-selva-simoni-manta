package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Team;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProfilesController implements GUIController {
    private Stage stage;
    private Scene scene;
    private GUI gui;

    List<Team> teams;

    private List<ImageView> guiPlayers;
    private List<Text> guiNicknames;
    private List<Text> guiTeams;
    private List<Text> guiEmojis;
    private List<Text> guiCoins;
    private List<ImageView> guiAssistants;
    private List<Button> guiShowSchoolBoards;

    @FXML
    private ImageView playerImage1, playerImage2, playerImage3, playerImage4;

    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4;

    @FXML
    private Text nickname1, nickname2, nickname3, nickname4;

    @FXML
    private Text team1, team2, team3, team4;

    @FXML
    private Text emoji1, emoji2, emoji3, emoji4;

    @FXML
    private Text coins1, coins2, coins3, coins4;

    @FXML
    private Button showSchoolboard1, showSchoolboard2, showSchoolboard3, showSchoolboard4;

    @FXML
    private Button realm;

    public void onRun() {
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

        //Hide everything on first draw
        guiShowSchoolBoards.forEach(btn -> btn.setVisible(false));

        realm.setOnAction(e -> {
            gui.changeScene("realm.fxml");
        });

        Platform.runLater(() -> {
            Game currentGame = gui.getClientController().getGameInfo();
            List<Player> players = currentGame.getPlayers();
            teams = currentGame.getTeams();
            players.forEach(this::drawPlayer);
        });
    }

    public void drawPlayer(Player player){

        int id = player.getPlayerId();

        //Player Image
        guiPlayers.get(id).setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player" + (id + 1) + ".jpg")));

        //Assistant played
        if(player.getLastAssistantPlayed() != null){
            //Played assistant, show it
            guiAssistants.get(id).setImage(new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (" + player.getLastAssistantPlayed().getAssistantId() + ").png")));
        } else {
            //Still need to play assistant, show generic back
            guiAssistants.get(id).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
            //If this is the current player, he can click on it to play an assistant
            guiAssistants.get(id).setOnMouseClicked(e -> {
                gui.changeScene("assistants.fxml");
            });
        }

        //Nickname
        guiNicknames.get(id).setText(player.getNickname());

        //Team
        guiTeams.get(id).setText("Team " + teams.get(id).getTeamId().toString());

        //Coins
        guiEmojis.get(id).setText("💰");
        guiCoins.get(id).setText(player.getCoins().toString());

        //School board button
        guiShowSchoolBoards.get(id).setVisible(true);
        guiShowSchoolBoards.get(id).setOnAction(e -> {
            gui.changeScene("schoolboard.fxml");
            System.out.println("ANCORA DA FINIRE PER BENE, DA METTERE OGNI SCHOOLBOARD");
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
