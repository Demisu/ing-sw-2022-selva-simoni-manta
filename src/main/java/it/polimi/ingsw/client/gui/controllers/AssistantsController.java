package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Character;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssistantsController implements GUIController {
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button button;

    @FXML
    private ImageView assistant1, assistant2, assistant3, assistant4, assistant5,
                        assistant6, assistant7, assistant8, assistant9, assistant10;


    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene("realm.fxml");
        /*Parent root = FXMLLoader.load(getClass().getResource("/realm.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
    }

    public int getIndexOfAssistant(int priority, int movement) {
        List<Assistant> assistants = gui.getClientController().getPlayerInfo().getDeck();
        for(Assistant assistant : assistants) {
            if (assistant.getTurnPriority() == priority && assistant.getMotherNatureMovements() == movement) {
                return assistants.indexOf(assistant);
            }
        }
        return 0;
    }

    public void onAssistant(MouseEvent event) {
        Platform.runLater(() -> {
            //MainGUIController controller = (MainGUIController) gui.getControllerFromName("assistants.fxml");
            //conroller.dostuff...
            //controller.godTile(Card.parseInput(((Button) event.getSource()).getText()), choose);

            //RIMPIAZZABILI CON UN BUTTON DA CLICKARE PER OGNUNO CON IL TEXT CONTENENTE IL LORO NUMERO
            //E POI TIPO: controller.godTile(Card.parseInput(((Button) event.getSource()).getText()), choose);
            assistant1.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(1,1));
            });

            assistant2.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(2,1));
            });

            assistant3.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(3,2));

            });

            assistant4.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(4,2));

            });

            assistant5.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(5,3));

            });

            assistant6.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(6,3));

            });

            assistant7.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(7,4));

            });

            assistant8.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(8,4));

            });

            assistant9.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(9,5));

            });

            assistant10.setOnMouseClicked(mouseEvent -> {
                gui.getClientController().playAssistant(getIndexOfAssistant(10,5));

            });
        });
    }

    public void onRun() {
        Platform.runLater(() -> {
            List<Assistant> deck = gui.getClientController().getPlayerInfo().getDeck();
            int[] array = new int[10];
            for(int i=0;i<10;i++){
                array[i]=0;
            }
            for (Assistant assistant : deck) {
                array[deck.indexOf(assistant)]=1;
            }
            int id = gui.getClientController().getPlayerInfo().getPlayerId()+1;
            if(array[0]==1){ // I REALLY NEED TO CLEAN THIS OUT, BUT PLEASE SPARE MY LIFE, I TRULY JUST WANTED EVERYTHING TO WORK, I SOLD MY SOUL FOR THIS
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (1).png"));
                assistant1.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant1.setImage(image);
            }
            if(array[1]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (2).png"));
                assistant2.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant2.setImage(image);
            }
            if(array[2]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (3).png"));
                assistant3.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant3.setImage(image);
            }
            if(array[3]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (4).png"));
                assistant4.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant4.setImage(image);
            }
            if(array[4]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (5).png"));
                assistant5.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant5.setImage(image);
            }
            if(array[5]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (6).png"));
                assistant6.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant6.setImage(image);
            }
            if(array[6]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (7).png"));
                assistant7.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant7.setImage(image);
            }
            if(array[7]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (8).png"));
                assistant8.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant8.setImage(image);
            }
            if(array[8]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (9).png"));
                assistant9.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant9.setImage(image);
            }
            if(array[9]==1){
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/2x/Assistente (10).png"));
                assistant10.setImage(image);
            }else{
                Image image = new Image(getClass().getResourceAsStream("/assets/Assistenti/retro/player"+id+".png"));
                assistant10.setImage(image);
            }
        });
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
