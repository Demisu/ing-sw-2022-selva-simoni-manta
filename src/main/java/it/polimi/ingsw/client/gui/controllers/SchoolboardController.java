package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.model.Color.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

/**
 * Controller responsible for managing the player's SchoolBoard
 * @see GUIController
 */
public class SchoolboardController implements GUIController {
    private GUI gui;

    @FXML
    private Button realm, gameStatus, undo;
    @FXML
    private Button colorBtn, sourceStudentsBtn, targetStudentsBtn, targetPiecesBtn, characterChecklistBtn;

    @FXML
    private ImageView greenProfessor, redProfessor, yellowProfessor, pinkProfessor, blueProfessor;
    @FXML
    private ImageView
            greenStudent1, greenStudent2, greenStudent3, greenStudent4, greenStudent5,
            greenStudent6, greenStudent7, greenStudent8, greenStudent9, greenStudent10,
            redStudent1, redStudent2, redStudent3, redStudent4, redStudent5,
            redStudent6, redStudent7, redStudent8, redStudent9, redStudent10,
            yellowStudent1, yellowStudent2, yellowStudent3, yellowStudent4, yellowStudent5,
            yellowStudent6, yellowStudent7, yellowStudent8, yellowStudent9, yellowStudent10,
            pinkStudent1, pinkStudent2, pinkStudent3, pinkStudent4, pinkStudent5,
            pinkStudent6, pinkStudent7, pinkStudent8, pinkStudent9, pinkStudent10,
            blueStudent1, blueStudent2, blueStudent3, blueStudent4, blueStudent5,
            blueStudent6, blueStudent7, blueStudent8, blueStudent9, blueStudent10;
    @FXML
    private ImageView greenStudent, redStudent, yellowStudent, pinkStudent, blueStudent;
    @FXML
    private ImageView tower1, tower2, tower3, tower4, tower5, tower6, tower7, tower8;

    @FXML
    private Pane guiDiningRoom;

    @FXML
    private Text greenNumber, redNumber, yellowNumber, pinkNumber, blueNumber;

    private Map<Color, ImageView> colorToImage;
    private Map<Color, Text> colorToText;
    private List<ImageView> guiTowers;
    private HashMap<Color, ImageView> professorsImage;
    private HashMap<Color, List<ImageView>> studentsDining;
    private HashMap<Color, Text> studentNumberTexts;
    private HashMap<Color, ImageView> studentImages;
    private ArrayList<Button> characterButtons;

    private HashMap<it.polimi.ingsw.model.Color, Integer> colorMapNumberChosen = new HashMap<>(){
        {
            put(GREEN, 0);
            put(YELLOW, 0);
            put(RED, 0);
            put(BLUE, 0);
            put(PURPLE, 0);
        }
    };
    private HashMap<it.polimi.ingsw.model.Color, Integer> colorMapNumberChosenDining = new HashMap<>(){
        {
            put(GREEN, 0);
            put(YELLOW, 0);
            put(RED, 0);
            put(BLUE, 0);
            put(PURPLE, 0);
        }
    };

    /**
     * Draws the overall SchoolBoard scene based on the player's information
     * @param player the player whose information is to be used to draw the SchoolBoard accordingly
     */
    public void drawSchoolBoard(Player player){
        drawStudentsEntrance(player.getPlayerBoard());
        drawStudentsDining(player);
        drawProfessors(player.getPlayerBoard().getProfessors());
        drawTowers(player);
        guiDiningRoom.setOnMouseClicked(e -> {
            if(gui.getStatus().equals(GUI.STUDENT)){
                //Student action
                gui.setStudentTarget(player.getPlayerBoard().getPieceID());
                //Move student
                Platform.runLater(() -> {
                    //Move student
                    if(!gui.getClientController().moveStudent(gui.getStudentToMove(), gui.getStudentSource(), gui.getStudentTarget())){
                        //If failed to move, tell the player
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot move student", ButtonType.OK);
                        alert.showAndWait();
                    }
                    resetStatus();
                    //Reload school board
                    gui.changeScene(GUI.SCHOOLBOARD);
                    gui.getControllerFromName(GUI.SCHOOLBOARD).onLoad();
                    scheduleRedraw();
                });
            }
        });
    }

    /**
     * Displays professors based on the current game data
     * @param professors the array of boolean values that indicate whether the player owns each professor or not
     */
    public void drawProfessors(Boolean[] professors) {
        for(Color color : Color.values()) {
            for (int i = 0; i < professors.length; i++) {
                if (professors[indexOfColor(color)]){
                    professorsImage.get(color).setImage(gui.getColorImage(color));
                    professorsImage.get(color).setEffect(new DropShadow());
                    professorsImage.get(color).setVisible(true);
                } else {
                    professorsImage.get(color).setVisible(false);
                }
            }
        }
    }

    /**
     * Draws the students in the dining area of the SchoolBoard scene based on the player's information
     * @param player the player whose information is to be used to draw the Dining Room accordingly
     */
    public void drawStudentsDining(Player player) {
        SchoolBoard schoolBoard = player.getPlayerBoard();
        for(Color color : Color.values()) {
            int number = schoolBoard.getDiningRoomStudents(color);
            for (int i = 0; i < number; i++) {
                ImageView studentToDraw = studentsDining.get(color).get(i);
                studentToDraw.setImage(gui.getColorImage(color));
                studentToDraw.setEffect(new DropShadow());
                studentToDraw.setVisible(true);
                studentToDraw.setOnMouseClicked(e -> {
                    System.out.println("click");
                    if(gui.getStatus().equals(GUI.CHARACTER)
                            && colorMapNumberChosenDining.get(color) < schoolBoard.getDiningRoomStudents(color)){
                        System.out.println(colorMapNumberChosenDining.get(color) + " < " + schoolBoard.getDiningRoomStudents(color));
                        gui.addStudent(schoolBoard.getAllDiningRoomStudents(color).get(colorMapNumberChosenDining.get(color)));
                        System.out.println("added " + schoolBoard.getAllDiningRoomStudents(color).get(colorMapNumberChosenDining.get(color)));
                        //+1 to counter of students of that color
                        colorMapNumberChosenDining.put(color, colorMapNumberChosenDining.get(color) + 1);
                        gui.addPiece(schoolBoard.getPieceID());
                    }
                });
            }
        }
    }

    /**
     * Draws the students in the entrance area of the SchoolBoard
     * @param schoolBoard the SchoolBoard of which the Students in the entrance area are to be drawn
     */
    public void drawStudentsEntrance(SchoolBoard schoolBoard) {
        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        schoolBoard.getStudents().forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);

        for(Color color : Color.values()) {
            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            if(tempStudentNumber > 0){
                //Set color
                studentNumberTexts.get(color).setText(String.valueOf(tempStudentNumber));
                studentImages.get(color).setImage(gui.getColorImage(color));
                studentImages.get(color).setEffect(new DropShadow());
                //Show them
                studentNumberTexts.get(color).setVisible(true);
                studentImages.get(color).setVisible(true);
                //Mouse click
                if(gui.getClientController().getGamePhase().equals(GamePhase.ACTION)) {
                    studentNumberTexts.get(color).setOnMouseClicked(e -> {
                        chooseStudent(schoolBoard, color);
                    });
                    studentImages.get(color).setOnMouseClicked(e -> {
                        chooseStudent(schoolBoard, color);
                    });
                }
            } else {
                studentNumberTexts.get(color).setVisible(false);
                studentImages.get(color).setVisible(false);
            }
        }
    }

    /**
     * Draws the Towers of the SchoolBoard scene based on the player's information
     * @param player the player whose information is to be used to draw the Towers accordingly
     */
    public void drawTowers(Player player){
        Team team = gui.getClientController().getGameInfo().getTeamByID(player.getTeamID());
        //If this player is the team leader, show towers
        if(team.getPlayers().get(0).getNickname().equals(player.getNickname())){
            for(int i = 0; i < team.getTowerNumber(); i++){
                ImageView towerToRender = guiTowers.get(i);
                towerToRender.setImage(gui.getColorImage(team.getTowerColor()));
                towerToRender.setEffect(new DropShadow());
                towerToRender.setVisible(true);
            }
        }
    }

    /**
     * Method which gets called upon switching to this scene, to prepare it to be displayed with up-to-date information
     */
    public void onLoad(){
        colorToImage = new HashMap<>(){
            {
                put(Color.YELLOW, yellowStudent);
                put(Color.RED, redStudent);
                put(Color.BLUE, blueStudent);
                put(Color.PURPLE, pinkStudent);
                put(Color.GREEN, greenStudent);
            }
        };
        colorToText = new HashMap<>() {
            {
                put(Color.YELLOW, yellowNumber);
                put(Color.RED, redNumber);
                put(Color.BLUE, blueNumber);
                put(Color.PURPLE, pinkNumber);
                put(Color.GREEN, greenNumber);
            }
        };

        studentNumberTexts = new HashMap<>() {
            {
                put(Color.YELLOW, yellowNumber);
                put(Color.RED, redNumber);
                put(Color.BLUE, blueNumber);
                put(Color.PURPLE, pinkNumber);
                put(Color.GREEN, greenNumber);
            }
        };
        studentImages = new HashMap<>() {
            {
                put(Color.YELLOW, yellowStudent);
                put(Color.RED, redStudent);
                put(Color.BLUE, blueStudent);
                put(Color.PURPLE, pinkStudent);
                put(Color.GREEN, greenStudent);
            }
        };
        studentsDining = new HashMap<>(){
            {
                put(Color.YELLOW, Arrays.asList(
                        yellowStudent1, yellowStudent2, yellowStudent3, yellowStudent4, yellowStudent5,
                        yellowStudent6, yellowStudent7, yellowStudent8, yellowStudent9, yellowStudent10
                ));
                put(Color.RED, Arrays.asList(
                        redStudent1, redStudent2, redStudent3, redStudent4, redStudent5,
                        redStudent6, redStudent7, redStudent8, redStudent9, redStudent10
                ));
                put(Color.BLUE, Arrays.asList(
                        blueStudent1, blueStudent2, blueStudent3, blueStudent4, blueStudent5,
                        blueStudent6, blueStudent7, blueStudent8, blueStudent9, blueStudent10
                ));
                put(Color.PURPLE, Arrays.asList(
                        pinkStudent1, pinkStudent2, pinkStudent3, pinkStudent4, pinkStudent5,
                        pinkStudent6, pinkStudent7, pinkStudent8, pinkStudent9, pinkStudent10
                ));
                put(Color.GREEN, Arrays.asList(
                        greenStudent1, greenStudent2, greenStudent3, greenStudent4, greenStudent5,
                        greenStudent6, greenStudent7, greenStudent8, greenStudent9, greenStudent10
                ));
            }
        };
        professorsImage = new HashMap<>(){
            {
                put(Color.GREEN, greenProfessor);
                put(Color.RED, redProfessor);
                put(Color.YELLOW, yellowProfessor);
                put(Color.PURPLE, pinkProfessor);
                put(Color.BLUE, blueProfessor);
            }
        };
        guiTowers = new ArrayList<>(){
            {
                add(tower1);
                add(tower2);
                add(tower3);
                add(tower4);
                add(tower5);
                add(tower6);
                add(tower7);
                add(tower8);
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

        for(Color color : Color.values()){
            //Hide students in entrance before drawing them
            studentImages.get(color).setVisible(false);
            studentNumberTexts.get(color).setVisible(false);
            //Hide students in dining before drawing them
            studentsDining.get(color).forEach(student -> student.setVisible(false));
            //Hide professors before drawing them
            professorsImage.get(color).setVisible(false);
        }
        //Hide towers before drawing them
        guiTowers.forEach(tower -> tower.setVisible(false));

        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            gui.getControllerFromName(GUI.PROFILES).onLoad();
        });

        //Show undo if an action is in progress
        undo.setVisible(!gui.getStatus().equals(GUI.NONE));
        undo.setOnAction(e -> {
            resetStatus();
            gui.changeScene(GUI.SCHOOLBOARD);
            gui.getControllerFromName(GUI.SCHOOLBOARD).onLoad();
            scheduleRedraw();
        });

        //Hide/Show needed characters buttons
        for(int i = 0; i < characterButtons.size(); i++){
            characterButtons.get(i).setDisable(!gui.listOfCharacterButtons().get(i));
        }
        characterChecklistBtn.setVisible(gui.getStatus().equals(GUI.CHARACTER));
        //Add onClick
        setCharacterButtons();
    }

    /**
     * Resets the data structure holding information about the Students to move
     */
    public void resetStatus(){

        gui.resetStatus();
        colorMapNumberChosen = new HashMap<>(){
            {
                put(GREEN, 0);
                put(YELLOW, 0);
                put(RED, 0);
                put(BLUE, 0);
                put(PURPLE, 0);
            }
        };
        colorMapNumberChosenDining = new HashMap<>(){
            {
                put(GREEN, 0);
                put(YELLOW, 0);
                put(RED, 0);
                put(BLUE, 0);
                put(PURPLE, 0);
            }
        };
        gui.reloadScene();
        //Hide undo
        undo.setVisible(false);
    }

    /**
     * Picks a Student when clicked on it
     * @param schoolBoard the SchoolBoard on which the Student is located
     * @param color the Color of the Student
     */
    public void chooseStudent(SchoolBoard schoolBoard, Color color){
        if(gui.getStatus().equals(GUI.NONE)) {
            gui.setStatus(GUI.STUDENT);
            gui.setStudentToMove(schoolBoard.getStudents(color).get(0));
            gui.setStudentSource(schoolBoard.getPieceID());
            undo.setVisible(true);
        } else if(gui.getStatus().equals(GUI.CHARACTER)
                && colorMapNumberChosen.get(color) < schoolBoard.getStudents(color).size()){
            gui.addStudent(schoolBoard.getStudents(color).get(colorMapNumberChosen.get(color)));
            //+1 to counter of students of that color
            colorMapNumberChosen.put(color, colorMapNumberChosen.get(color) + 1);
            gui.addPiece(schoolBoard.getPieceID());
        }
    }

    /**
     *  Sets the choosingObject for moving pieces upon clicking the corresponding button
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

    /**
     *  Checks if gameModel updated, if so redraws the SchoolBoard with up-to-date data
     */
    public void scheduleRedraw(){
        ScheduledExecutorService lateUpdater = Executors.newSingleThreadScheduledExecutor();
        lateUpdater.scheduleAtFixedRate(() -> {
            if(gui.isReady()){
                ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).drawSchoolBoard(gui.getClientController().getPlayerInfo());
                lateUpdater.shutdown();
            }
        }, 5, 5, TimeUnit.MILLISECONDS);
    }

    /**
     * @param gui the GUI object to set
     */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
