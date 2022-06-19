package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

import static it.polimi.ingsw.model.GamePhase.ACTION;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static javafx.scene.paint.Color.WHITE;

public class RealmController implements GUIController {

    private Stage stage;
    private GUI gui;
    public Boolean expertMode = true; //Might want to make this Static
    public final int studentSize = 40;
    public final int oddX = 30;
    public final int evenX = 10;
    public final int startingY = 10;
    public final int textOffsetX = 15;
    public final int textOffsetY = 27;

    //Statuses
    private final String NONE = "NONE";
    private final String MOTHER_NATURE = "MOTHER_NATURE";
    private final String STUDENT = "STUDENT";

    //Status for actions like mother nature movement
    private String status = NONE;

    //Variables for positioning
    int counterX = oddX;
    int counterY = startingY;
    int colorCounter = 0;

    //Variables for mother nature movement
    Integer originIslandIndex;
    Integer targetIslandIndex;

    @FXML
    private Button characters, assistants, schoolboard, menu, profiles, students, motherNature, pass, undo;

    @FXML
    private Pane stackPane1, stackPane2, stackPane3, stackPane4, stackPane5, stackPane6, stackPane7, stackPane8, stackPane9, stackPane10, stackPane11, stackPane12;
    @FXML
    private Pane cloudStackPane1, cloudStackPane2, cloudStackPane3, cloudStackPane4;

    @FXML
    private ImageView island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11, island12;
    @FXML
    private ImageView cloud1, cloud2, cloud3, cloud4;
    @FXML
    private ImageView bag;

    private ArrayList<ImageView> guiIslands;
    private ArrayList<ImageView> guiClouds;
    private ArrayList<Pane> islandPane;
    private ArrayList<Pane> cloudPane;

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
        islandPane = new ArrayList<>(){
            {
                add(stackPane1);
                add(stackPane2);
                add(stackPane3);
                add(stackPane4);
                add(stackPane5);
                add(stackPane6);
                add(stackPane7);
                add(stackPane8);
                add(stackPane9);
                add(stackPane10);
                add(stackPane11);
                add(stackPane12);
            }
        };
        cloudPane = new ArrayList<>(){
            {
                add(cloudStackPane1);
                add(cloudStackPane2);
                add(cloudStackPane3);
                add(cloudStackPane4);
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
        undo.setVisible(false);
        undo.setOnAction(e -> resetStatus());
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
        pass.setOnAction(e -> {
            Platform.runLater(() -> {
                gui.getClientController().passTurn();
            });
        });

        //TO POLISH
        //Show student button only during action phase
        students.setVisible(gui.getClientController().getGamePhase().equals(ACTION));
        students.setOnAction(e -> {
            //[TO BE IMPLEMENTED..]
        });

        //OLD
        //motherNature.setOnAction(e -> {
        //    motherNatureDialog(gui.getClientController().getPlayerInfo().getLastAssistantPlayed().getMotherNatureMovements() + Game.getMotherNatureMovements());
        //});

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
        Integer index = island.getPieceID();
        ImageView islandToRender = guiIslands.get(index);
        islandToRender.setVisible(true);
        islandToRender.setCursor(Cursor.HAND);
        islandToRender.setOnMouseClicked(e -> {
            if(status.equals(MOTHER_NATURE)){
                targetIslandIndex = gui.getClientController().getIslands().indexOf(island);
                Platform.runLater(() -> {
                    //Move mother nature
                    gui.getClientController().moveMotherNature(targetIslandIndex - originIslandIndex);
                    resetStatus();
                });
            } else {
                gui.changeScene(GUI.ISLAND);
                ((IslandController) gui.getControllerFromName(GUI.ISLAND)).onLoad();
                ((IslandController) gui.getControllerFromName(GUI.ISLAND)).drawZoomedIsland(island);
            }
        });
        drawStudents(islandPane.get(index), island.getStudents(), island);

        counterX = oddX;
        if(island.isMotherNature()) {
            //Draw mother nature
            ImageView motherNature = new ImageView();
            motherNature.setImage(new Image(getClass().getResourceAsStream("/assets/coin.png")));
            islandPane.get(index).getChildren().add(motherNature);
            motherNature.setLayoutX(counterX);
            motherNature.setLayoutY(counterY);
            motherNature.setFitHeight(studentSize);
            motherNature.setFitWidth(studentSize);
            ColorAdjust effect = new ColorAdjust();
            effect.setBrightness(-0.21);
            effect.setContrast(-0.19);
            effect.setSaturation(1.0);
            effect.setHue(0.09);
            motherNature.setEffect(effect);

            //Add hand if it's action phase, player's turn
            if(gui.getClientController().getGamePhase().equals(ACTION)
                && gui.getClientController().getPlayerInfo().getNickname().equals(gui.getClientController().getGameInfo().getCurrentPlayer())) {

                //Add movement on click
                motherNature.setOnMouseClicked(e -> {
                    //Set origin
                    originIslandIndex = gui.getClientController().getIslands().indexOf(island);
                    //Update status
                    status = MOTHER_NATURE;
                    undo.setVisible(true);
                });
                motherNature.setCursor(Cursor.MOVE);

            } else {
                motherNature.setCursor(Cursor.WAIT);
            }
        }
        if(island.getTowersNumber() > 0) {
            //Draw tower
            ImageView tower = new ImageView();
            counterX += studentSize;
            tower = new ImageView();
            tower.setImage(new Image(getClass().getResourceAsStream("/assets/coin.png")));
            islandPane.get(index).getChildren().add(tower);
            tower.setLayoutX(counterX);
            tower.setLayoutY(counterY);
            tower.setFitHeight(studentSize);
            tower.setFitWidth(studentSize);
            ColorAdjust effect = new ColorAdjust();
            switch (island.getTowersColor()) {
                case WHITE -> {
                    effect.setBrightness(0.5);
                }
                case BLACK -> {
                    effect.setBrightness(-0.9);
                }
                case GREY -> {
                    effect.setBrightness(-0.6);
                }
            }
            effect.setContrast(1);
            effect.setSaturation(-1.0);
            effect.setHue(0);
            tower.setEffect(effect);
        }
    }

    public void drawCloud(Cloud cloud){
        int index = (cloud.getPieceID() - 12 ) / 2;
        ImageView cloudToRender = guiClouds.get(index);
        cloudToRender.setVisible(true);
        cloudToRender.setCursor(Cursor.HAND);
        cloudToRender.setOnMouseClicked(e -> {
            gui.changeScene(GUI.CLOUD);
            ((CloudController) gui.getControllerFromName(GUI.CLOUD)).onLoad();
            ((CloudController) gui.getControllerFromName(GUI.CLOUD)).drawZoomedCloud(cloud);
        });
        drawStudents(cloudPane.get(index), cloud.getStudents(), cloud);
    }

    public void drawStudents(Pane studentWrapper, HashSet<Integer> students, StudentAccessiblePiece piece){

        counterX = oddX;
        counterY = startingY;
        colorCounter  = 0;

        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        //Count each color
        students.forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);

        //Draw each color
        for(it.polimi.ingsw.model.Color color : it.polimi.ingsw.model.Color.values()){

            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            ImageView colorImage = new ImageView();
            Text colorNumber = new Text();

            //If there are no students of this color, skip it
            if(tempStudentNumber == 0){
                colorImage.setVisible(false);
                colorNumber.setVisible(false);
            } else {
                colorImage.setImage(new Image(getClass().getResourceAsStream("/assets/coin.png")));
                studentWrapper.getChildren().add(colorImage);

                //Image
                colorImage.setLayoutX(counterX);
                colorImage.setLayoutY(counterY);
                colorImage.setFitHeight(studentSize);
                colorImage.setFitWidth(studentSize);
                colorImage.setEffect(getColorEffect(color));

                //Text
                studentWrapper.getChildren().add(colorNumber);
                colorNumber.setLayoutX(counterX + textOffsetX);
                colorNumber.setLayoutY(counterY + textOffsetY);
                colorNumber.setFont(new Font("System Bold", 20.0));
                colorNumber.setFill(WHITE);
                colorNumber.setEffect(new DropShadow());
                colorNumber.setTextAlignment(TextAlignment.CENTER);
                colorNumber.setText(tempStudentNumber.toString());

                //Set on click (if cloud)
                if(piece instanceof Cloud cloud && gui.getClientController().getGamePhase().equals(ACTION)){
                    colorImage.setOnMouseClicked(e -> selectCloud(cloud));
                    colorNumber.setOnMouseClicked(e -> selectCloud(cloud));
                    colorImage.setCursor(Cursor.HAND);
                    colorNumber.setCursor(Cursor.HAND);
                } else {
                    colorImage.setCursor(Cursor.WAIT);
                    colorNumber.setCursor(Cursor.WAIT);
                }
            }

            //Setup next
            colorCounter++;
            if(colorCounter == 2 | colorCounter == 5){
                counterY += studentSize;
                counterX = evenX;
            }else {
                counterX += studentSize;
            }
        }
    }

    public ColorAdjust getColorEffect(it.polimi.ingsw.model.Color color){
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.14);
        colorAdjust.setContrast(1.0);
        colorAdjust.setSaturation(1.0);
        switch (color) {
            case YELLOW -> {
                colorAdjust.setHue(0.21);
            }
            case RED -> {}
            case GREEN -> {
                colorAdjust.setHue(0.47);
            }
            case BLUE -> {
                colorAdjust.setHue(1.0);
            }
            case PURPLE -> {
                colorAdjust.setHue(-0.36);
            }
        }
        return colorAdjust;
    }

    public void selectCloud(Cloud cloud){
        Player currentPlayer = gui.getClientController().getGameInfo().getPlayerByNickname(gui.getClientController().getPlayerInfo().getNickname());
        cloud.getStudents().forEach(student -> {
            Platform.runLater(() -> {
                gui.getClientController().moveStudent(student, cloud.getPieceID(), currentPlayer.getPlayerBoard().getPieceID());
                gui.getClientController().passTurn();
            });
        });
    }

    public void resetStatus(){

        status = NONE;
        originIslandIndex = 0;
        targetIslandIndex = 0;

        //Hide undo
        undo.setVisible(false);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
