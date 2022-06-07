package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.gui.controllers.AssistantsController;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUI extends Application implements ClientView {

    public static final String END_OF_THE_GAME = "End of the game";
    private static final String MENU = "start.fxml";
    private static final String NICKNAME = "nickname.fxml";
    private static final String PLAYERS = "players.fxml";
    private static final String REALM = "realm.fxml";
    private static final String SCHOOLBOARD = "schoolboard.fxml";
    private static final String ASSISTANTS = "assistants.fxml";

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

    public static void main(String[] args) throws IOException  {
        launch(args);
    }

    public GUI(){
        this.clientController = null;
    }

    public GUI(ClientController clientController){
        this.clientController = clientController;
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

    /**
     * Method setup creates all the stage phases which will be updated in other methods, in particular:
     * - MENU: the game's main menu with Play and Quit buttons;
     * - SETUP: small windows containing player's inserted nickname, the server IP and port;
     * - LOADER: the game loader containing player's chosen color, god power and places their workers;
     * - GUI: the effective game GUI (island board).
     * Each stage scene is put inside an hashmap, which links their name to their fxml filename.
     */
    public void setup() {
        List<String> fxmList = new ArrayList<>(Arrays.asList(MENU, NICKNAME, PLAYERS, REALM, SCHOOLBOARD, ASSISTANTS));
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
        this.changeRoot("start"); // start  -  players  -  action  -  realm
        primaryStage.setTitle("Eriantys");
        GUIController assistantsController = new AssistantsController();
        assistantsController.setGui(this);
        //primaryStage.getIcons().add(new Image("file:C:\\Users\\dario\\Desktop\\University\\Prog. di Ing. del Software\\ing-sw-2022-selva-simoni-manta\\src\\main\\resources\\assets\\icon.png"));
        primaryStage.setMaximized(false);
        primaryStage.setMinWidth(1295);
        //primaryStage.setMaxWidth(1295);
        primaryStage.setMinHeight(758);
        //primaryStage.setMaxHeight(758);
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    /**
     * @see Application#stop()
     */
    @Override
    public void stop() {
        System.exit(0);
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