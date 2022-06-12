package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.gui.controllers.AssistantsController;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GUI extends Application implements ClientView {

    public static final String END_OF_THE_GAME = "End of the game";
    private static final String MENU = "start.fxml";
    private static final String NICKNAME = "nickname.fxml";
    private static final String PLAYERS = "players.fxml";
    private static final String REALM = "realm.fxml";
    private static final String SCHOOLBOARD = "schoolboard.fxml";
    private static final String ASSISTANTS = "assistants.fxml";
    private static final String ISLAND1 = "island1.fxml";
    private static final String ISLAND2 = "island2.fxml";
    private static final String ISLAND3 = "island3.fxml";
    private static final String CLOUD = "cloud.fxml";
    private static final String LOBBY = "lobby.fxml";

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
    //private ConnectionSocket connectionSocket = null;
    //private boolean activeGame;
    private Scene currentScene;
    private Stage stage;
    //private MediaPlayer player;
    //private boolean[] actionCheckers;

    private ClientController clientController;
    private Parent root;
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

    public void changeRoot(String name) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/" + name + ".fxml"));
    }

    public void changeScene(String scene){
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
     * Each stage scene is put inside an hashmap, which links their name to their fxml filename.
     */
    public void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, NICKNAME, PLAYERS, REALM, SCHOOLBOARD, ASSISTANTS, ISLAND1, ISLAND2, ISLAND3, CLOUD, LOBBY));
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
        /*
        try {
            this.changeRoot("start"); // start  -  players  -  action  -  realm
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Eriantys");
        GUIController assistantsController = new AssistantsController();
        assistantsController.setGui(this);
        //primaryStage.getIcons().add(new Image("file:C:\\Users\\dario\\Desktop\\University\\Prog. di Ing. del Software\\ing-sw-2022-selva-simoni-manta\\src\\main\\resources\\assets\\icon.png"));
        stage.setMaximized(false);
        stage.setMinWidth(1295);
        //primaryStage.setMaxWidth(1295);
        stage.setMinHeight(758);
        //primaryStage.setMaxHeight(758);
        stage.setScene(new Scene(root, 1280, 720));
        stage.show();
        */
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