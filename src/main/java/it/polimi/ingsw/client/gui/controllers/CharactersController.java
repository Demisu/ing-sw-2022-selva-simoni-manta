package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Character;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CharactersController implements GUIController {


    private final Image coinImage = new Image(getClass().getResourceAsStream("/assets/coin.png"));
    private Stage stage;
    private Scene scene;
    private GUI gui;

    @FXML
    private Button info1, info2, info3;
    private ArrayList<Button> info;

    @FXML
    private ImageView character1, character2, character3, coin1, coin2, coin3;
    private ArrayList<ImageView> guiCharacter, coins;

    public void switchToRealmScene(ActionEvent e) throws IOException {
        gui.changeScene(GUI.REALM);
    }

    public void onRun() {

        guiCharacter = new ArrayList<>(){
           {
               add(character1);
               add(character2);
               add(character3);
           }
        };
        info = new ArrayList<>(){
            {
                add(info1);
                add(info2);
                add(info3);
            }
        };
        coins = new ArrayList<>(){
            {
                add(coin1);
                add(coin2);
                add(coin3);
            }
        };

        Platform.runLater(() -> {

            ArrayList<Character> characters = gui.getClientController().getCharacters();
            for(int i=0;i<3;i++){
                //characters.get(i).getEffect() //This needs to be implemented in Character Class
                info.forEach(info -> info.setOnAction(e -> {
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    final Stage dialog = new Stage();
                    Pane bagRoot = new Pane();
                    StackPane bagHolder = new StackPane();
                    Canvas canvas = new Canvas(2000,2000);
                    bagHolder.getChildren().add(canvas);
                    bagRoot.getChildren().add(bagHolder);
                    Scene bagScene = new Scene(bagRoot, 600, 400);
                    dialog.setScene(bagScene);
                    dialog.initModality(Modality.NONE);
                    dialog.initOwner(stage);
                    dialog.getIcons().add(new Image(getClass().getResourceAsStream("/assets/coin.png")));
                    dialog.setTitle("[CHARACTER NAME TO BE ADDED..]");
                    VBox dialogVbox = new VBox(20);
                    dialogVbox.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                    dialogVbox.getChildren().add(new Text("\t [INFO TO BE ADDED..]"));
                    Scene dialogScene = new Scene(dialogVbox, 300, 200);
                    dialog.setScene(dialogScene);
                    dialog.show();
                }));
            }
            for (Character character : characters){
                //Available character face up
                if(!character.getHasBeenUsed()){
                    int index = characters.indexOf(character);
                    guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/CarteTOT_front" + character.getImage() + ".jpg")));
                    guiCharacter.get(index).setOnMouseClicked(mouseEvent -> {
                        //Play it
                        Platform.runLater(() -> {
                            if(gui.getClientController().playCharacter(index)){
                                //Turn the card face down
                                guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                            } else {
                                System.out.println("No");
                            }
                        });
                    });
                    if (character.getHasIncreasedCost()) {
                        coins.get(index).setImage(coinImage);
                    }
                }else{
                    int index = characters.indexOf(character);
                    guiCharacter.get(index).setImage(new Image(getClass().getResourceAsStream("/assets/Personaggi/Personaggi_retro.jpg")));
                }
            }
        });
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

}
