package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements ClientView {

    public static final String END_OF_THE_GAME = "End of the game";
    public static final String MENU = "start.fxml";
    public static final String NICKNAME = "nickname.fxml";
    public static final String PLAYERS = "players.fxml";
    public static final String REALM = "realm.fxml";
    public static final String SCHOOLBOARD = "schoolboard.fxml";
    public static final String ASSISTANTS = "assistants.fxml";
    public static final String ISLAND = "island.fxml";
    public static final String CLOUD = "cloud.fxml";
    public static final String LOBBY = "lobby.fxml";
    public static final String CHARACTERS = "characters.fxml";
    public static final String PROFILES = "profiles.fxml";

    /**
     * Maps each scene name to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();

    /**
     * Maps each scene controller's name to the effective controller object, in order to get the correct controller
     * for modifying operations.
     *
     * @see it.polimi.ingsw.client.gui.controllers for more details.
     */
    private final HashMap<String, GUIController> nameMapController = new HashMap<>();
    private Scene currentScene;
    private Stage stage;

    private MediaPlayer musicPlayer;

    private ClientController clientController;
    private static ClientController staticClientController;

    public static void main(String[] args) throws IOException  {
        launch(args);
    }

    public GUI(){
        this.clientController = staticClientController;
    }

    public GUI(ClientController clientController){
        this.clientController = clientController;
        staticClientController = clientController;
        try {
            main(new String[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public ClientController getClientController() {
        return clientController;
    }

    public void changeScene(String scene){
        //If setup is completed, start updating the model
        if(! (scene.equals(MENU) ||  scene.equals(NICKNAME) || scene.equals(PLAYERS))) {
            Platform.runLater(() -> clientController.getModelInfo());
        }
        currentScene = nameMapScene.get(scene);
        double prevWidth = stage.getWidth();
        double prevHeight = stage.getHeight();
        stage.setHeight(prevHeight);
        stage.setWidth(prevWidth);
        stage.setScene(currentScene);
        stage.show();
    }

    /**
     * Method setup creates all the stage phases which will be updated in other methods, in particular:
     * - MENU: the game's main menu with Play and Quit buttons;
     * - SETUP: small windows containing player's inserted nickname, the server IP and port;
     * - LOADER: the game loader containing player's chosen color, god power and places their workers;
     * - GUI: the effective game GUI (island board).
     * Each stage scene is put inside a hashmap, which links their name to their fxml filename.
     */
    public void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, NICKNAME, PLAYERS, REALM, SCHOOLBOARD, ASSISTANTS, ISLAND, CLOUD, LOBBY, CHARACTERS, PROFILES));
        try {
            for (String name : fxmList) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + name));
                nameMapScene.put(name, new Scene(loader.load()));
                GUIController controller = loader.getController();
                controller.setGui(this);
                nameMapController.put(name, controller);
            }
        } catch (IOException e) {
            System.out.println("Error in GUI setup");
        }
        currentScene = nameMapScene.get(MENU);
    }

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        setup();
        this.stage = primaryStage;
        run();
    }

    public void run() {
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icon.png")));
        stage.show();

        Media music = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("assets/media/Epic_Orchestral_Music.mp3")).toExternalForm());
        musicPlayer = new MediaPlayer(music);
        musicPlayer.setAutoPlay(true);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(25);
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.seek(Duration.ZERO);
            musicPlayer.play();
        });
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void createModal(Stage stage, String title, String asset, Color color, String info){
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
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/assets/"+asset)));
        dialog.setTitle(title);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        dialogVbox.getChildren().add(new Text(info));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * @see Application#stop()
     */
    @Override
    public void stop() {
        System.exit(0);
    }

    /**
     * Method getControllerFromName gets a scene controller based on inserted name from the dedicated hashmap.
     *
     * @param name of type String - the player's name.
     * @return GUIController - the scene controller.
     */
    public GUIController getControllerFromName(String name) {
        return nameMapController.get(name);
    }

    @Override
    public void waitGameStartPhase() {

    }

    @Override
    public void setupPhase() {

    }

    @Override
    public void planningPhase() {

    }

    @Override
    public void actionPhase() {

    }
}