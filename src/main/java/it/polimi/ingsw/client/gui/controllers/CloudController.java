package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Cloud;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Island;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

/**
 * controller for the zoomed clouds scene in the GUI
 */
public class CloudController implements GUIController {
    private Stage stage;
    private GUI gui;

    @FXML
    private Button realm;

    @FXML
    private ImageView guiCloud;
    @FXML
    private ImageView yellowStudent, redStudent, blueStudent, pinkStudent, greenStudent;

    @FXML
    private Text yellowNumber, redNumber, blueNumber, pinkNumber, greenNumber;

    private Map<Color, ImageView> colorToImage;
    private Map<Color, Text> colorToText;

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
        colorToText = new HashMap<>(){
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
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
    }

    /**
     * @param cloud the cloud to be filled with data
     */
    public void drawZoomedCloud(Cloud cloud){
        guiCloud.setImage(new Image(getClass().getResourceAsStream("/assets/cloud-zoomed.png")));
        drawStudents(cloud.getStudents());
    }

    /**
     * @param students students currently present, to be showed
     */
    public void drawStudents(HashSet<Integer> students){

        Integer[] studentsNumber = new Integer[5];
        Arrays.fill(studentsNumber, 0);
        //Count each color
        students.forEach(student -> studentsNumber[indexOfColor(colorOfStudent(student))]++);
        //Draw each color
        for (Color color : Color.values()) {
            Integer tempStudentNumber = studentsNumber[indexOfColor(color)];
            colorToImage.get(color).setImage(gui.getColorImage(color));
            colorToImage.get(color).setEffect(new DropShadow());
            colorToImage.get(color).setVisible(tempStudentNumber > 0);
            colorToText.get(color).setVisible(tempStudentNumber > 0);
            //Set text to number of students of that color
            colorToText.get(color).setText(tempStudentNumber.toString());
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
