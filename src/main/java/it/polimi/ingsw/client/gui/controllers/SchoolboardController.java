package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.*;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

public class SchoolboardController implements GUIController {
    private GUI gui;

    @FXML
    private Button realm, gameStatus;

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
    private Text greenNumber, redNumber, yellowNumber, pinkNumber, blueNumber;

    private Map<Color, ImageView> colorToImage;

    private Map<Color, Text> colorToText;


    public void drawSchoolBoard(Player player){// This schoolBoard is just to show, without functionality
        drawStudentsEntrance(player.getPlayerBoard().getStudents());
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.GREEN), Color.GREEN);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.RED), Color.RED);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.YELLOW), Color.YELLOW);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.PURPLE), Color.PURPLE);
        drawStudentsDining(player.getPlayerBoard().getAllDiningRoomStudents(Color.BLUE), Color.BLUE);
        drawProfessors(player.getPlayerBoard().getProfessors());
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
        });

        yellowNumber.setOnMouseClicked(e -> {
            /*if(gui.getClientController().getGamePhase().equals(GamePhase.ACTION) &&
                    gui.getClientController().getPlayerInfo().getNickname().equals(gui.getClientController().getGameInfo().getCurrentPlayer())) {
                Platform.runLater(() -> {
                    gui.getClientController().moveStudent(
                            gui.getClientController().getPlayerInfo().getPlayerBoard().getStudents().stream().filter(s ->
                            colorOfStudent(s).equals(Color.YELLOW)).findFirst().orElseThrow(),
                            gui.getClientController().getPlayerInfo().getPlayerBoard().getPieceID(),
                            gui.);
                    gui.getClientController().
                });
            }*/
        });
        //[TO BE IMPLEMENTED..]
    }

    public void drawProfessors(Boolean[] professors) {
        HashMap<Color, ImageView> professorsImage = new HashMap<>(){
            {
                put(Color.GREEN, greenProfessor);
                put(Color.RED, redProfessor);
                put(Color.YELLOW, yellowProfessor);
                put(Color.PURPLE, pinkProfessor);
                put(Color.BLUE, blueProfessor);
            }
        };

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
        HashMap<Color, List<ImageView>> studentsDining = new HashMap<>(){
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
        if(students.size() != 0) {
            for (int i = 0; i < students.size(); i++) {
                studentsDining.get(color).get(i).setVisible(true);
                studentsDining.get(color).get(i).setEffect(gui.getColorEffect(color));
            }
        }
    }

    public void drawStudentsEntrance(HashSet<Integer> students) {
        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        students.forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);

        HashMap<Color, Text> studentNumberTexts = new HashMap<>() {
            {
                put(Color.YELLOW, yellowNumber);
                put(Color.RED, redNumber);
                put(Color.BLUE, blueNumber);
                put(Color.PURPLE, pinkNumber);
                put(Color.GREEN, greenNumber);
            }
        };

        HashMap<Color, ImageView> studentImages = new HashMap<>() {
            {
                put(Color.YELLOW, yellowStudent);
                put(Color.RED, redStudent);
                put(Color.BLUE, blueStudent);
                put(Color.PURPLE, pinkStudent);
                put(Color.GREEN, greenStudent);
            }
        };

        for(Color color : Color.values()) {
            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            studentNumberTexts.get(color).setText(String.valueOf(tempStudentNumber));
            studentImages.get(color).setEffect(gui.getColorEffect(color));
        }
    }

//These two functions might not be enough to reset the old state of the FXML between them!
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

        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            ((RealmController) gui.getControllerFromName(GUI.REALM)).onLoad();
        });
        gameStatus.setOnAction(e -> {
            gui.changeScene(GUI.PROFILES);
            ((ProfilesController) gui.getControllerFromName(GUI.PROFILES)).onLoad();
        });
        //[TO BE IMPLEMENTED..]
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
