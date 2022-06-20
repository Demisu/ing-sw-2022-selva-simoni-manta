package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;
import it.polimi.ingsw.model.Character;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

public class CharactersController implements GUIController {
    private final Image coinImage = new Image(getClass().getResourceAsStream("/assets/coin.png"));
    private Stage stage;
    private GUI gui;

    @FXML
    private Text coinsDisplay;

    @FXML
    private Button realm;
    @FXML
    private Button info1, info2, info3;

    @FXML
    private ImageView character1, character2, character3, coin1, coin2, coin3;

    private ArrayList<Button> info;
    private ArrayList<ImageView> guiCharacter, coins;

    public void onLoad() {
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
        realm.setOnAction(e -> {
            gui.changeScene(GUI.REALM);
            gui.getControllerFromName(GUI.REALM).onLoad();
        });
        Platform.runLater(() -> {
            coinsDisplay.setText("💰 " + gui.getClientController().getPlayerInfo().getCoins());
            ArrayList<Character> characters = gui.getClientController().getCharacters();
            for(int i=0;i<3;i++){
                //characters.get(i).getEffect() //This needs to be implemented in Character Class
                info.forEach(info -> info.setOnAction(e -> {
                    stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    ArrayList<Text> infoChars = new ArrayList<>(){
                        {
                            add(new Text("\t [INFO TO BE ADDED..]"));
                        }
                    };
                    gui.createModal(stage, "[CHARACTER NAME TO BE ADDED..]", "coin.png", Color.DARKGRAY, infoChars);
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
