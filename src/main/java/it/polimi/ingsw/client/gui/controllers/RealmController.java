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
import javafx.scene.paint.Paint;
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
    public ArrayList<Integer> studentsInBag;
    public String bagInfo;
    public final int studentSize = 40;
    public final int oddX = 30;
    public final int evenX = 10;
    public final int startingY = 10;
    public final int textOffsetX = 15;
    public final int textOffsetY = 27;

    //Variables for positioning
    int counterX = oddX;
    int counterY = startingY;
    int colorCounter = 0;

    @FXML
    private Button characters, assistants, schoolboard, menu, profiles, pass, undo;
    @FXML
    private Button colorBtn, sourceStudentsBtn, sourcePiecesBtn, targetStudentsBtn, targetPiecesBtn;

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
    private ArrayList<Button> characterButtons;
    private ArrayList<String> colors;

    public void onLoad(){
        colors = new ArrayList<>(){
            {
                add("YELLOW");
                add("BLUE");
                add("GREEN");
                add("RED");
                add("PURPLE");
            }
        };
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
        characterButtons = new ArrayList<>(){
            {
                add(colorBtn);
                add(sourceStudentsBtn);
                add(sourcePiecesBtn);
                add(targetStudentsBtn);
                add(targetPiecesBtn);
            }
        };

        //Clear islands
        islandPane.forEach(pane -> pane.getChildren().clear());
        //Clear clouds
        cloudPane.forEach(pane -> pane.getChildren().clear());
        //Hide/Show needed characters buttons
        for(int i = 0; i < characterButtons.size(); i++){
            characterButtons.get(i).setVisible(gui.listOfCharacterButtons().get(i));
            //TODO ADD ONCLICK
        }

        if(!expertMode){
            characters.setVisible(false);
        }else{
            characters.setOnAction(e -> {
                gui.changeScene(GUI.CHARACTERS);
                gui.getControllerFromName(GUI.CHARACTERS).onLoad();
            });
        }

        //Show undo if an action is in progress
        undo.setVisible(!gui.getStatus().equals(GUI.NONE));
        undo.setOnAction(e -> resetStatus());

        assistants.setOnAction(e -> {
            gui.changeScene(GUI.ASSISTANTS);
            gui.getControllerFromName(GUI.ASSISTANTS).onLoad();
        });
        schoolboard.setOnAction(e -> {
            gui.changeScene(GUI.SCHOOLBOARD);
            gui.getControllerFromName(GUI.SCHOOLBOARD).onLoad();
            ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).drawSchoolBoard(gui.getClientController().getPlayerInfo());
        });
        menu.setOnAction(e -> gui.changeScene(GUI.MENU));
        profiles.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            gui.getControllerFromName(GUI.PROFILES).onLoad();
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
            ArrayList<Text> info = new ArrayList<>();
            studentsInBag = gui.getClientController().getGameInfo().getBagStudents();
            bagInfo = "Students remaining: " + studentsInBag.size() + "\n\n";
            info.add(new Text(bagInfo));
            Text emoji;
            for (it.polimi.ingsw.model.Color color : it.polimi.ingsw.model.Color.values()) {
                emoji = new Text();
                emoji.setText("\uD83D\uDD34");
                emoji.setFill(parseColor(color));
                bagInfo = color + ": " + studentsInBag.stream().filter(s -> colorOfStudent(s).equals(color)).count() + " students\n";
                info.add(emoji);
                info.add(new Text(bagInfo));
            }
            gui.createModal(stage, "Bag", "bag.png", Color.BURLYWOOD, info);
        });
    }

    public Paint parseColor(it.polimi.ingsw.model.Color color){
        switch(color){
            case YELLOW -> {
                return Color.YELLOW;
            }
            case BLUE -> {
                return Color.BLUE;
            }
            case GREEN -> {
                return Color.GREEN;
            }
            case RED -> {
                return Color.RED;
            }
            case PURPLE -> {
                return Color.PURPLE;
            }
            default -> {
                return Color.BLACK;
            }
        }
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
            if(gui.getStatus().equals(GUI.MOTHER_NATURE)){
                gui.setTargetIslandIndex(gui.getClientController().getIslands().indexOf(island));
                Platform.runLater(() -> {
                    //Move mother nature
                    gui.getClientController().moveMotherNature(gui.getTargetIslandIndex() - gui.getOriginIslandIndex());
                    resetStatus();
                    //Reload realm
                    gui.getClientController().getModelInfo();
                    gui.changeScene(GUI.REALM);
                    ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
                });
            } else if(gui.getStatus().equals(GUI.STUDENT)){
                //Student action
                gui.setStudentTarget(island.getPieceID());
                //Move student
                Platform.runLater(() -> {
                    //Move student
                    gui.getClientController().moveStudent(gui.getStudentToMove(), gui.getStudentSource(), gui.getStudentTarget());
                    resetStatus();
                    //Reload realm
                    gui.getClientController().getModelInfo();
                    gui.changeScene(GUI.REALM);
                    ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
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
            //Delete old mother nature
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
                    gui.setOriginIslandIndex(gui.getClientController().getIslands().indexOf(island));
                    //Update status
                    gui.setStatus(GUI.MOTHER_NATURE);
                    undo.setVisible(true);
                });
                motherNature.setCursor(Cursor.MOVE);

            } else {
                motherNature.setCursor(Cursor.WAIT);
            }
        }else if(island.getNoEntry() > 0){
            //Draw no entry on mother nature's place
            ImageView motherNature = new ImageView();
            motherNature.setImage(new Image(getClass().getResourceAsStream("/assets/noEntry.png")));
            islandPane.get(index).getChildren().add(motherNature);
            motherNature.setLayoutX(counterX);
            motherNature.setLayoutY(counterY);
            motherNature.setFitHeight(studentSize);
            motherNature.setFitWidth(studentSize);
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
            tower.setEffect(gui.getColorEffect(island.getTowersColor()));
        }
    }

    public void drawCloud(Cloud cloud){
        int index = (cloud.getPieceID() - 12 ) / 2;
        ImageView cloudToRender = guiClouds.get(index);
        cloudToRender.setVisible(true);
        if(gui.getClientController().getGamePhase().equals(ACTION)){
            //Click to move students
            cloudToRender.setOnMouseClicked(e -> selectCloud(cloud));
            cloudPane.get(index).setOnMouseClicked(e -> selectCloud(cloud));
            cloudToRender.setCursor(Cursor.HAND);
            cloudPane.get(index).setCursor(Cursor.HAND);
        } else {
            //Click to zoom
            cloudPane.get(index).setCursor(Cursor.HAND);
            cloudToRender.setCursor(Cursor.HAND);
            cloudToRender.setOnMouseClicked(e -> {
                gui.changeScene(GUI.CLOUD);
                ((CloudController) gui.getControllerFromName(GUI.CLOUD)).onLoad();
                ((CloudController) gui.getControllerFromName(GUI.CLOUD)).drawZoomedCloud(cloud);
            });
        }
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
                colorImage.setEffect(gui.getColorEffect(color));

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

    public void selectCloud(Cloud cloud){
        Player currentPlayer = gui.getClientController().getGameInfo().getPlayerByNickname(gui.getClientController().getPlayerInfo().getNickname());
        Platform.runLater(() -> {
            for(Integer student : cloud.getStudents()){
                gui.getClientController().moveStudent(student, cloud.getPieceID(), currentPlayer.getPlayerBoard().getPieceID());
            }
            gui.getClientController().getModelInfo();
            gui.changeScene(GUI.REALM);
        });
    }

    public void resetStatus(){

        gui.resetStatus();
        //Hide undo
        undo.setVisible(false);
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
