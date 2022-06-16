package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

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
        });
        menu.setOnAction(e -> {
            gui.changeScene(GUI.MENU);
            //Might want to remove this Button
        });
        profiles.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
        });
        students.setOnAction(e -> {
            //[TO BE IMPLEMENTED..]
        });
        motherNature.setOnAction(e -> {
            //[TO BE IMPLEMENTED..]
        });
        pass.setOnAction(e -> {
            //[TO BE IMPLEMENTED..]
        });

        guiIslands.forEach(island -> island.setVisible(false));
        guiClouds.forEach(cloud -> cloud.setVisible(false));
        Platform.runLater(() -> {
            List<Island> islands = gui.getClientController().getIslands();
            List<Player> players= gui.getClientController().getGameInfo().getPlayers();
            islands.forEach(island -> {
                guiIslands.get(island.getPieceID()).setVisible(true);
                guiIslands.get(island.getPieceID()).setOnMouseClicked(e ->{
                    switch ((island.getPieceID()+1) % 3){
                        case 1 -> {
                            gui.changeScene(GUI.ISLAND1);
                            ((IslandController) gui.getControllerFromName(GUI.ISLAND1)).onLoad();
                        }
                        case 2 -> {
                            gui.changeScene(GUI.ISLAND3);
                            ((IslandController) gui.getControllerFromName(GUI.ISLAND3)).onLoad();
                        }
                        case 0 -> {
                            gui.changeScene(GUI.ISLAND2);
                            ((IslandController) gui.getControllerFromName(GUI.ISLAND2)).onLoad();
                        }
                    }
                });
            });
            for(int i=0;i<players.size();i++){
                guiClouds.get(i).setVisible(true);
                guiClouds.get(i).setOnMouseClicked(e -> {
                    gui.changeScene(GUI.CLOUD);
                    ((CloudController) gui.getControllerFromName(GUI.CLOUD)).onLoad();
                });
            }
        });
        bag.setOnMouseClicked(e -> {
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            gui.createModal(stage, "Bag", "bag.png", Color.BURLYWOOD, "\t [INFO TO BE ADDED..]");
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
