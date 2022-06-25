package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

import static it.polimi.ingsw.model.Color.*;
import static it.polimi.ingsw.model.Color.PURPLE;
import static it.polimi.ingsw.model.GamePhase.ACTION;
import static it.polimi.ingsw.model.GamePhase.PLANNING;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.WHITE;

/**
 * controller for the whole realm GUI scene
 */
public class RealmController implements GUIController {

    private Stage stage;
    private GUI gui;
    /**
     * true for expert mode, false otherwise
     */
    public Boolean expertMode = true; //Might want to make this Static
    /**
     * all the students inside the game bag
     */
    public ArrayList<Integer> studentsInBag;
    /**
     * text to be shown when the bag is opened
     */
    public String bagInfo;
    /**
     * diameter size for the GUI
     */
    public final int studentSize = 40;
    /**
     * odd rows for the GUI
     */
    public final int oddX = 30;
    /**
     * even rows for the GUI
     */
    public final int evenX = 10;
    /**
     * starting Y position for the GUI
     */
    public final int startingY = 10;
    /**
     * X Offset intended for the GUI positioning
     */
    public final int textOffsetX = 15;
    /**
     * Y Offset intended for the GUI positioning
     */
    public final int textOffsetY = 27;

    //Variables for positioning
    int counterX = oddX;
    int counterY = startingY;
    int colorCounter = 0;

    @FXML
    private Button characters, assistants, schoolboard, menu, profiles, pass, undo;
    @FXML
    private Button colorBtn, sourceStudentsBtn, targetStudentsBtn, targetPiecesBtn, characterChecklistBtn;

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
    private HashMap<it.polimi.ingsw.model.Color, Integer> colorMapNumberChosen = new HashMap<>(){
        {
            put(it.polimi.ingsw.model.Color.GREEN, 0);
            put(YELLOW, 0);
            put(RED, 0);
            put(BLUE, 0);
            put(PURPLE, 0);
        }
    };

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
                add(targetStudentsBtn);
                add(targetPiecesBtn);
            }
        };

        if(gui.getStatus().equals(GUI.MOTHER_NATURE)){
            int i = 0;
            for(ImageView guiIsland : guiIslands){
                //If island is visibile and in range for mother nature (and she hasn't been moved already), highlight it
                if(guiIsland.isVisible()
                        && (!gui.getClientController().getGameInfo().getMovedMotherNatureInTurn())
                        && (i - gui.getOriginIslandIndex()) > 0
                        && (i - gui.getOriginIslandIndex()) <= (gui.getClientController().getPlayerInfo().getLastAssistantPlayed().getMotherNatureMovements()
                                                                + gui.getClientController().getGameInfo().getMotherNatureMovementsModifier()) ) {
                    guiIsland.setEffect(gui.getHighlight(GREEN));
                }
                i++;
            }
        } else if(gui.getStatus().equals(GUI.STUDENT)) {
            guiIslands.forEach(guiIsland -> guiIsland.setEffect(gui.getHighlight(GREEN)));
        } else {
            guiIslands.forEach(guiIsland -> guiIsland.setEffect(null));
        }

        //Clear islands
        islandPane.forEach(pane -> pane.getChildren().clear());
        //Clear clouds
        cloudPane.forEach(pane -> pane.getChildren().clear());

        //Hide/Show needed characters buttons
        for(int i = 0; i < characterButtons.size(); i++){
            characterButtons.get(i).setDisable(!gui.listOfCharacterButtons().get(i));
        }
        characterChecklistBtn.setVisible(gui.getStatus().equals(GUI.CHARACTER));
        //Add onClick
        setCharacterButtons();

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
        undo.setOnAction(e -> {
            resetStatus();
            gui.reloadScene();
        });

        assistants.setOnAction(e -> {
            gui.changeScene(GUI.ASSISTANTS);
            gui.getControllerFromName(GUI.ASSISTANTS).onLoad();
        });
        schoolboard.setOnAction(e -> {
            gui.changeScene(GUI.SCHOOLBOARD);
            gui.getControllerFromName(GUI.SCHOOLBOARD).onLoad();
            ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).drawSchoolBoard(gui.getClientController().getPlayerInfo());
        });
        menu.setOnAction(e -> gui.stop());
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
                bagInfo = (color.equals(PURPLE) ? "PINK" : color) + ": " + studentsInBag.stream().filter(s -> colorOfStudent(s).equals(color)).count() + " students\n";
                info.add(emoji);
                info.add(new Text(bagInfo));
            }
            gui.createModal(stage, "Bag", "bag.png", Color.BURLYWOOD, info);
        });
    }

    /**
     * @param color color from model
     * @return color from javafx
     */
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
                return Color.PINK;
            }
            default -> {
                return Color.BLACK;
            }
        }
    }

    /**
     * @param island island to be filled with data (students)
     */
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
                    int steps = gui.getTargetIslandIndex() - gui.getOriginIslandIndex();
                    if(steps > 0
                            && (steps <= gui.getClientController().getPlayerInfo().getLastAssistantPlayed().getMotherNatureMovements()
                                            + gui.getClientController().getGameInfo().getMotherNatureMovementsModifier()) ) {
                        gui.getClientController().moveMotherNature(gui.getTargetIslandIndex() - gui.getOriginIslandIndex());
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Moving too far!", ButtonType.OK);
                        alert.showAndWait();
                    }
                    resetStatus();
                    guiIslands.forEach(guiIsland -> guiIsland.setEffect(null));
                    //Reload realm
                    gui.getClientController().getModelInfo();
                    gui.changeScene(GUI.REALM);
                    gui.getControllerFromName(GUI.REALM).onLoad();
                });
            } else if(gui.getStatus().equals(GUI.STUDENT)){
                //Student action
                gui.setStudentTarget(island.getPieceID());
                //Move student
                Platform.runLater(() -> {
                    //Move student
                    if(!gui.getClientController().moveStudent(gui.getStudentToMove(), gui.getStudentSource(), gui.getStudentTarget())){
                        //If failed to move, tell the player
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot move student", ButtonType.OK);
                        alert.showAndWait();
                    }
                    resetStatus();
                    //Reload realm
                    gui.getClientController().getModelInfo();
                    gui.changeScene(GUI.REALM);
                    gui.getControllerFromName(GUI.REALM).onLoad();
                });
            } else if(gui.getStatus().equals(GUI.CHARACTER)){
                gui.addPiece(island.getPieceID());
            } else {
                gui.changeScene(GUI.ISLAND);
                gui.getControllerFromName(GUI.ISLAND).onLoad();
                ((IslandController) gui.getControllerFromName(GUI.ISLAND)).drawZoomedIsland(island);
            }
        });
        drawStudents(islandPane.get(index), island.getStudents(), island);
        counterX = oddX;
        if(island.isMotherNature()) {
            //Delete old mother nature
            //Draw mother nature
            ImageView motherNature = new ImageView();
            motherNature.setImage(new Image(getClass().getResourceAsStream("/assets/mothernature.png")));
            islandPane.get(index).getChildren().add(motherNature);
            motherNature.setLayoutX(counterX);
            motherNature.setLayoutY(counterY);
            motherNature.setFitHeight(studentSize);
            motherNature.setFitWidth(studentSize);
            motherNature.setEffect(new DropShadow());

            //Add hand if it's action phase, player's turn
            if(gui.getClientController().getGamePhase().equals(ACTION)
                && gui.getClientController().getPlayerInfo().getNickname().equals(gui.getClientController().getGameInfo().getCurrentPlayer())
                && !gui.getClientController().getGameInfo().getMovedMotherNatureInTurn()) {

                //Add movement on click
                motherNature.setOnMouseClicked(e -> {
                    //Set origin
                    gui.setOriginIslandIndex(gui.getClientController().getIslands().indexOf(island));
                    //Update status
                    gui.setStatus(GUI.MOTHER_NATURE);
                    undo.setVisible(true);
                    gui.reloadScene();
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
            tower.setImage(gui.getColorImage(island.getTowersColor()));
            islandPane.get(index).getChildren().add(tower);
            tower.setLayoutX(counterX);
            tower.setLayoutY(counterY);
            tower.setFitHeight(studentSize);
            tower.setFitWidth(studentSize);
            tower.setEffect(new DropShadow());
            //Text
            Text towerNumber = new Text();
            islandPane.get(index).getChildren().add(towerNumber);
            towerNumber.setLayoutX(counterX + textOffsetX);
            towerNumber.setLayoutY(counterY + textOffsetY);
            towerNumber.setFont(new Font("System Bold", 20.0));
            towerNumber.setFill(WHITE);
            towerNumber.setEffect(new DropShadow());
            towerNumber.setTextAlignment(TextAlignment.CENTER);
            towerNumber.setText(island.getTowersNumber().toString());
        }
    }

    /**
     * @param cloud cloud to be filled with data (students)
     */
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
                gui.getControllerFromName(GUI.CLOUD).onLoad();
                ((CloudController) gui.getControllerFromName(GUI.CLOUD)).drawZoomedCloud(cloud);
            });
        }
        drawStudents(cloudPane.get(index), cloud.getStudents(), cloud);
    }

    /**
     * @param studentWrapper wrapper pane for the students
     * @param students students to be drawn
     * @param piece piece on which to place the students
     */
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
                colorImage.setImage(gui.getColorImage(color));
                colorImage.setEffect(new DropShadow());

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
                } else if(gui.getStatus().equals(GUI.CHARACTER)
                        && colorMapNumberChosen.get(color) < piece.getStudents(color).size()){
                    gui.addStudent(piece.getStudents(color).get(colorMapNumberChosen.get(color)));
                    //+1 to counter of students of that color
                    colorMapNumberChosen.put(color, colorMapNumberChosen.get(color) + 1);
                    gui.addPiece(piece.getPieceID());

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

    /**
     * @param cloud cloud selected for the task associated
     */
    public void selectCloud(Cloud cloud){
        Player currentPlayer = gui.getClientController().getGameInfo().getPlayerByNickname(gui.getClientController().getPlayerInfo().getNickname());
        Platform.runLater(() -> {
            for(Integer student : cloud.getStudents()){
                if(!gui.getClientController().moveStudent(student, cloud.getPieceID(), currentPlayer.getPlayerBoard().getPieceID())){
                    //If failed, alert player
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot select cloud", ButtonType.OK);
                    alert.showAndWait();
                    break;
                }
            }
            gui.getClientController().getModelInfo();
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
    }

    /**
     * see gui.resetStatus()
     */
    public void resetStatus(){
        gui.resetStatus();
        colorMapNumberChosen = new HashMap<>(){
            {
                put(it.polimi.ingsw.model.Color.GREEN, 0);
                put(YELLOW, 0);
                put(RED, 0);
                put(BLUE, 0);
                put(PURPLE, 0);
            }
        };
        //Hide undo
        undo.setVisible(false);
    }

    /**
     * just set the "on click" of the buttons
     */
    public void setCharacterButtons(){
        colorBtn.setOnAction(e -> {
            gui.colorDialog();
        });
        sourceStudentsBtn.setOnAction(e -> {
            gui.setChoosingObject(GUI.STUDENTSINORIGIN);
        });
        targetStudentsBtn.setOnAction(e -> {
            gui.setChoosingObject(GUI.STUDENTSINTARGET);
        });
        targetPiecesBtn.setOnAction(e -> {
            gui.setChoosingObject(GUI.TARGET);
        });
        characterChecklistBtn.setOnAction(e -> {
            gui.characterChecklist();
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
