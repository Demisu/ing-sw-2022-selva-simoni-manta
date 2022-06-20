package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

public class SchoolboardController implements GUIController {
    private GUI gui;

    @FXML
    private Button realm, gameStatus, undo;

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

    public void drawSchoolBoard(Player player){// This schoolBoard is just to show, without functionality
        drawStudentsEntrance(player.getPlayerBoard());
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.GREEN), Color.GREEN);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.RED), Color.RED);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.YELLOW), Color.YELLOW);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.PURPLE), Color.PURPLE);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.BLUE), Color.BLUE);
        drawProfessors(player.getPlayerBoard().getProfessors());
        drawTowers(player);
        guiDiningRoom.setOnMouseClicked(e -> {
            if(gui.getStatus().equals(GUI.STUDENT)){
                //Student action
                gui.setStudentTarget(player.getPlayerBoard().getPieceID());
                //Move student
                Platform.runLater(() -> {
                    //Move student
                    gui.getClientController().moveStudent(gui.getStudentToMove(), gui.getStudentSource(), gui.getStudentTarget());
                    resetStatus();
                    //Reload school board
                    gui.getClientController().getModelInfo();
                    gui.changeScene(GUI.SCHOOLBOARD);
                    ((SchoolboardController) gui.getControllerFromName(GUI.SCHOOLBOARD)).onLoad();
                });
            }
        });
    }

    public void drawProfessors(Boolean[] professors) {

        for(Color color : Color.values()) {
            for (int i = 0; i < professors.length; i++) {
                if (professors[indexOfColor(color)]){
                    professorsImage.get(color).setVisible(true);
                    professorsImage.get(color).setEffect(gui.getColorEffect(color));
                } else {
                    professorsImage.get(color).setVisible(false);
                }
            }
        }
    }

    public void drawStudentsDining(ArrayList<Integer> students, Color color) {
        if(students.size() != 0) {
            for (int i = 0; i < students.size(); i++) {
                studentsDining.get(color).get(i).setVisible(true);
                studentsDining.get(color).get(i).setEffect(gui.getColorEffect(color));
            }
        }
    }

    public void drawStudentsEntrance(SchoolBoard schoolBoard) {
        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        schoolBoard.getStudents().forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);

        for(Color color : Color.values()) {
            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            if(tempStudentNumber > 0){
                //Set color
                studentNumberTexts.get(color).setText(String.valueOf(tempStudentNumber));
                studentImages.get(color).setEffect(gui.getColorEffect(color));
                //Show them
                studentNumberTexts.get(color).setVisible(true);
                studentImages.get(color).setVisible(true);
                //Mouse click
                studentNumberTexts.get(color).setOnMouseClicked(e -> {
                    gui.setStatus(GUI.STUDENT);
                    gui.setStudentToMove(schoolBoard.getStudents(color).get(0));
                    gui.setStudentSource(schoolBoard.getPieceID());
                    undo.setVisible(true);
                });
                studentImages.get(color).setOnMouseClicked(e -> {
                    gui.setStatus(GUI.STUDENT);
                    gui.setStudentToMove(schoolBoard.getStudents(color).get(0));
                    gui.setStudentSource(schoolBoard.getPieceID());
                    undo.setVisible(true);
                });
            } else {
                studentNumberTexts.get(color).setVisible(false);
                studentImages.get(color).setVisible(false);
            }
        }
    }

    public void drawTowers(Player player){
        Team team = gui.getClientController().getGameInfo().getTeamByID(player.getTeamID());
        //If this player is the team leader, show towers
        if(team.getPlayers().get(0).getNickname().equals(player.getNickname())){
            for(int i = 0; i < team.getTowerNumber(); i++){
                ImageView towerToRender = guiTowers.get(i);
                towerToRender.setImage(new Image(getClass().getResourceAsStream("/assets/coin.png")));
                towerToRender.setEffect(gui.getColorEffect(team.getTowerColor()));
                towerToRender.setVisible(true);
            }
        }
    }

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

        //Undo
        undo.setVisible(false);
        undo.setOnAction(e -> resetStatus());

        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
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
