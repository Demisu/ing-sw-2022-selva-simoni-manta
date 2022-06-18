package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.*;

public class RealmController implements GUIController {

    private Stage stage;
    private GUI gui;
    public Boolean expertMode = true; //Might want to make this Static

    @FXML
    private Button characters, assistants, schoolboard, menu, profiles, students, motherNature, pass;

    @FXML
    private ImageView island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;

    @FXML
    private ImageView cloud1, cloud2, cloud3, cloud4;

    @FXML
    private ImageView bag;

    private ArrayList<ImageView> guiIslands;
    private ArrayList<ImageView> guiClouds;

    public void onLoad(){
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
        guiClouds = new ArrayList<>(){
            {
                add(cloud1);
                add(cloud2);
                add(cloud3);
                add(cloud4);
            }
        };
        if(!expertMode){
            characters.setVisible(false);
        }else{
            characters.setOnAction(e -> {
                gui.changeScene(GUI.CHARACTERS);
                ((CharactersController) gui.getControllerFromName(GUI.CHARACTERS)).onLoad();
            });
        }
        assistants.setOnAction(e -> {
            gui.changeScene(GUI.ASSISTANTS);
            ((AssistantsController) gui.getControllerFromName(GUI.ASSISTANTS)).onLoad();
        });
        schoolboard.setOnAction(e -> {
            gui.changeScene(GUI.SCHOOLBOARD);
            ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).onLoad();
            ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).drawSchoolBoard(gui.getClientController().getPlayerInfo());
        });
        menu.setOnAction(e -> gui.changeScene(GUI.MENU));
        profiles.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
        });
        //Show student button only during action phase
        students.setVisible(gui.getClientController().getGamePhase().equals(GamePhase.ACTION));
        students.setOnAction(e -> {
            //[TO BE IMPLEMENTED..]
        });
        motherNature.setOnAction(e -> {
            motherNatureDialog(gui.getClientController().getPlayerInfo().getLastAssistantPlayed().getMotherNatureMovements());
        });
        pass.setOnAction(e -> {
            Platform.runLater(() -> {
                gui.getClientController().passTurn();
            });
        });

        guiIslands.forEach(island -> island.setVisible(false));
        guiClouds.forEach(cloud -> cloud.setVisible(false));
        Platform.runLater(() -> {
            List<Island> islands = gui.getClientController().getIslands();
            List<Cloud> cloud = gui.getClientController().getClouds();
            List<Player> players = gui.getClientController().getGameInfo().getPlayers();
            islands.forEach(this::drawIsland);
            cloud.forEach(this::drawCloud);
        });
        bag.setOnMouseClicked(e -> {
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            gui.createModal(stage, "Bag", "bag.png", Color.BURLYWOOD, "\t [INFO TO BE ADDED..]");
        });
    }

    /**
     * Shows a dialog window to move Mother Nature by the defined number of steps
     *
     * @param max_steps the maximum number of steps that are to be allowed
     */
    public void motherNatureDialog(int max_steps) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(max_steps));
        dialog.setTitle("Move Mother Nature");
        dialog.setHeaderText("Maximum steps allowed: " + max_steps);
        dialog.setGraphic(null);
        Optional<String> movements = dialog.showAndWait();
        movements.ifPresent(string -> {
            if(Integer.parseInt(string) <= max_steps) {
                gui.getClientController().moveMotherNature(Integer.parseInt(string));
            } else {
                motherNatureDialog(max_steps);
            }
        });
    }

    public void drawIsland(Island island){
        ImageView islandToRender = guiIslands.get(island.getPieceID());
        islandToRender.setVisible(true);
        islandToRender.setCursor(Cursor.HAND);
        islandToRender.setOnMouseClicked(e -> {
            gui.changeScene(GUI.ISLAND);
            ((IslandController) gui.getControllerFromName(GUI.ISLAND)).onLoad();
            ((IslandController) gui.getControllerFromName(GUI.ISLAND)).drawZoomedIsland(island);
        });
        drawStudents(islandToRender, island.getStudents());
    }

    public void drawCloud(Cloud cloud){
        ImageView cloudToRender = guiClouds.get((cloud.getPieceID() - 12 ) / 2);
        cloudToRender.setVisible(true);
        cloudToRender.setCursor(Cursor.HAND);
        cloudToRender.setOnMouseClicked(e -> {
            gui.changeScene(GUI.CLOUD);
            ((CloudController) gui.getControllerFromName(GUI.CLOUD)).onLoad();
            ((CloudController) gui.getControllerFromName(GUI.CLOUD)).drawZoomedCloud(cloud);
        });
        drawStudents(cloudToRender, cloud.getStudents());
    }

    public void drawStudents(ImageView guiElement, HashSet<Integer> students){
        //TODO
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
