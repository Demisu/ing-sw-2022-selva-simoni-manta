package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.StudentAccessiblePiece;
import it.polimi.ingsw.model.TowerColor;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
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
 * controller for the zoomed islands GUI scene
 */
public class IslandController implements GUIController {
    private Stage stage;
    private GUI gui;

    @FXML
    private Button realm;

    @FXML
    private ImageView guiIsland;
    @FXML
    private ImageView yellowStudent, redStudent, blueStudent, pinkStudent, greenStudent, tower, motherNature;

    @FXML
    private Text yellowNumber, redNumber, blueNumber, pinkNumber, greenNumber, towerNumber;

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
     * @param island the island to be filled with data
     */
    public void drawZoomedIsland(Island island){
        guiIsland.setImage(new Image(getClass().getResourceAsStream("/assets/island" + ((island.getPieceID()) % 3 + 1) + "-zoomed.png")));
        drawStudents(island.getStudents());
        //If mother nature is present, show her
        motherNature.setImage(new Image(getClass().getResourceAsStream("/assets/mothernature.png")));
        motherNature.setEffect(new DropShadow());
        motherNature.setVisible(island.isMotherNature());
        //If there are towers, show them
        drawTowers(island);
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

    /**
     * @param island island that needs towers to be drawn
     */
    public void drawTowers(Island island){
        boolean hasTowers = island.getTowersNumber() > 0;
        tower.setVisible(hasTowers);
        towerNumber.setVisible(hasTowers);
        if(hasTowers){
            tower.setImage(gui.getColorImage(island.getTowersColor()));
            tower.setEffect(new DropShadow());
            towerNumber.setText(island.getTowersNumber().toString());
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
