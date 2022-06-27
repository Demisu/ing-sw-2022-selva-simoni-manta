package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.model.StudentAccessiblePiece;
import it.polimi.ingsw.model.TowerColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

/**
 * main class responsible for starting and handling the game GUI
 */
public class GUI extends Application implements ClientView {

    /** FXML filename for the start screen */
    public static final String MENU = "start.fxml";

    /** FXML filename for the nickname selection screen */
    public static final String NICKNAME = "nickname.fxml";

    /** FXML filename for the number of players selection screen during game setup */
    public static final String PLAYERS = "players.fxml";

    /** FXML filename for the realm screen */
    public static final String REALM = "realm.fxml";

    /** FXML filename for the player's SchooBoard screen */
    public static final String SCHOOLBOARD = "schoolboard.fxml";

    /** FXML filename for the player's Assistants screen */
    public static final String ASSISTANTS = "assistants.fxml";

    /** FXML filename for the zoomed in Island screen */
    public static final String ISLAND = "island.fxml";

    /** FXML filename for the zoomed in Cloud screen */
    public static final String CLOUD = "cloud.fxml";

    /** FXML filename for the waiting Lobby screen */
    public static final String LOBBY = "lobby.fxml";

    /** FXML filename for the Character selection screen */
    public static final String CHARACTERS = "characters.fxml";

    /** FXML filename for the game's Player information screen */
    public static final String PROFILES = "profiles.fxml";

    //Status for actions: Identifies the possible phases in which the game can be

    /** Identifies the game being in no particular phase */
    public static final String NONE = "NONE";

    /** Identifies the game being in the phase for moving Mother Nature */
    public static final String MOTHER_NATURE = "MOTHER_NATURE";

    /** Identifies the game being in the phase for handling a Student */
    public static final String STUDENT = "STUDENT";

    /** Identifies the game being in the phase for Character playing */
    public static final String CHARACTER = "CHARACTER";

    /** Identifies the game being in the phase for selecting the source location for moving a Student */
    public static final String STUDENTSINORIGIN = "STUDENTSINORIGIN";

    /** Identifies the game being in the phase for selecting the target location for moving a Student */
    public static final String STUDENTSINTARGET = "STUDENTSINTARGET";

    /** Identifies the game being in the phase for selecting the target location for a Character's effect */
    public static final String TARGET = "TARGET";

    //Status
    private String status = NONE;

    private boolean ready;

    //Used for student movement
    private Integer studentToMove;
    private Integer studentSource;
    private Integer studentTarget;

    //Variables for mother nature movement
    private Integer originIslandIndex;
    private Integer targetIslandIndex;

    //Variables for character buttons visibility
    private Boolean colorBtnVisible = false;
    private Boolean sourceStudentsBtnVisible = false;
    private Boolean targetStudentsBtnVisible = false;
    private Boolean targetPiecesBtnVisible = false;
    //Variables for character requests
    private Character currentCharacter = null;
    private int characterIndex = 0;
    private it.polimi.ingsw.model.Color characterTargetColor = null;
    private List<Integer> characterStudentsInOrigin = new ArrayList<>();
    private List<Integer> characterStudentsInTarget = new ArrayList<>();
    private List<Integer> characterOriginPieces = new ArrayList<>();
    private List<Integer> characterTargetPieces = new ArrayList<>();
    //Flags
    private String choosingObject = NONE;

    /**
     * Maps each scene name to the effective scene object, in order to easily find it during scene changing operations.
     */
    private final HashMap<String, Scene> nameMapScene = new HashMap<>();
    private final HashMap<Scene, String> sceneMapName = new HashMap<>();

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

    private ScheduledExecutorService updater;
    private boolean alreadyNotifiedEnd = false;

    /**
     * Runs the GUI with the specified input arguments
     * @param args starting arguments
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException  {
        launch(args);
    }

    /**
     * builds a GUI object assigning the clientController reference; this constructor is called by JavaFX
     */
    public GUI(){
        this.clientController = staticClientController;
    }

    /**
     * @param clientController the ClientController to assign
     * builds the GUI assigning the clientController reference and attempts to run it
     * @see ClientController
     */
    public GUI(ClientController clientController){
        this.clientController = clientController;
        staticClientController = clientController;
        try {
            main(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param clientController clientController to be set
     */
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * @return the current ClientController
     */
    public ClientController getClientController() {
        return clientController;
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
                Scene scene = new Scene(loader.load());
                nameMapScene.put(name, scene);
                sceneMapName.put(scene, name);
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

    /**
     * Executes the game with GUI
     * @see Application#start(Stage)
     */
    public void run() {
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icon.png")));
        stage.show();
;
        Media music = new Media(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("media/Epic_Orchestral_Music.mp3")).toExternalForm());
        musicPlayer = new MediaPlayer(music);
        musicPlayer.setAutoPlay(true);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.setVolume(25);
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.seek(Duration.ZERO);
            musicPlayer.play();
        });

        //GUI model updater
        updater = Executors.newSingleThreadScheduledExecutor();
        updater.scheduleAtFixedRate(this::reloadScene, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * @return the MediaPlayer for background music
     */
    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    /**
     * Changes the scene to the one specified
     *
     * @param scene the target scene to switch to
     *
     * @see GUI class variables
     */
    public synchronized void changeScene(String scene){
        //If setup is completed, start updating the model
        if(! (scene.equals(MENU) ||  scene.equals(NICKNAME) || scene.equals(PLAYERS))) {
            Platform.runLater(() -> {
                ready = false;
                clientController.getModelInfo();
                ready = true;
            });
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
     * Reloads the currently displaying Scene
     */
    public void reloadScene(){
        //Update the scene only when needed
        if(currentScene.equals(nameMapScene.get(LOBBY))
                || currentScene.equals(nameMapScene.get(REALM))
                || currentScene.equals(nameMapScene.get(PROFILES))
                || currentScene.equals(nameMapScene.get(ASSISTANTS)) ) {
            Platform.runLater(() -> {
                changeScene(sceneMapName.get(currentScene));
                this.getControllerFromName(sceneMapName.get(currentScene)).onLoad();
                if(!alreadyNotifiedEnd && clientController.getGameInfo() != null && clientController.getGamePhase().equals(GamePhase.END)){
                    alreadyNotifiedEnd = true;
                    changeScene(GUI.PROFILES);
                    getControllerFromName(GUI.PROFILES).onLoad();
                    updater.shutdown();
                }
            });
        } else if(!currentScene.equals(nameMapScene.get(MENU))
                && !currentScene.equals(nameMapScene.get(NICKNAME))
                && !currentScene.equals(nameMapScene.get(PLAYERS)) ) {
            clientController.getModelInfo();
        }
    }

    /**
     *  Displays a window with information about the Character currently in effect. The information is dynamically
     *  generated based on the Character that has been played.
     */
    public void characterChecklist() {
        ArrayList<Text> info = new ArrayList<>();
        if(colorBtnVisible){
            info.add(new Text("Color: "
                    + (characterTargetColor == null ? "Still need to choose" : String.valueOf(characterTargetColor))
                    + "\n"));
        }
        if(sourceStudentsBtnVisible){
            StringBuilder students = new StringBuilder();
            if(characterStudentsInOrigin.isEmpty()){
                students = new StringBuilder("Still need to choose");
            } else {
                for(Integer student : characterStudentsInOrigin){
                    students.append(" ").append(colorOfStudent(student));
                }
            }
            info.add(new Text("[" + characterStudentsInOrigin.size() + "/" + currentCharacter.getEffectNumberMax() + "] Source students: " + students + "\n"));
        }
        if(targetStudentsBtnVisible){
            StringBuilder students = new StringBuilder();
            if(characterStudentsInTarget.isEmpty()){
                students = new StringBuilder("Still need to choose");
            } else {
                for(Integer student : characterStudentsInTarget){
                    students.append(" ").append(colorOfStudent(student));
                }
            }
            info.add(new Text("[" + characterStudentsInTarget.size() + "/" + currentCharacter.getEffectNumberMax() + "] Target students: " + students + "\n"));
        }
        if(targetPiecesBtnVisible){
            StringBuilder pieces = new StringBuilder();
            if(characterTargetPieces.isEmpty()){
                pieces = new StringBuilder("Still need to choose");
            } else {
                for(Integer pieceID : characterTargetPieces){
                    pieces.append(" ").append(clientController.getGameInfo().getStudentAccessiblePieceByID(pieceID).getClass().getSimpleName());
                }
            }
            info.add(new Text("[" + characterTargetPieces.size() + "/" + currentCharacter.getEffectNumberMax() + "] Target pieces: " + pieces + "\n"));
        }

        if(info.isEmpty()){
            info.add(new Text("Character is ready, click on it to use it!"));
        }

        createModal(stage,
                "Character " + currentCharacter.getImage() + " checklist",
                "coin.png",
                Color.WHITE,
                info);
    }

    /**
     * Builds and displays a window, containing information that is passed to it, to the user
     * @param stage the Stage owner
     * @param title the title to assign
     * @param asset asset's filename in order to retrieve it, used as icon for the window in title bar
     * @param color the background color to be used
     * @param info  actual text to be displayed by the modal
     */
    public void createModal(Stage stage, String title, String asset, Color color, ArrayList<Text> info){
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
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/assets/" + asset)));
        dialog.setTitle(title);
        TextFlow dialogTflow = new TextFlow();
        dialogTflow.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        dialogTflow.getChildren().addAll(info);
        Scene dialogScene = new Scene(dialogTflow, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Shows a dialog window to select a Color
     * @see it.polimi.ingsw.model.Color
     */
    public void colorDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Select a color");
        dialog.setHeaderText("Allowed: " + Arrays.toString(it.polimi.ingsw.model.Color.values()));
        dialog.setGraphic(null);
        Optional<String> color = dialog.showAndWait();
        color.ifPresent(inputColor -> {
            it.polimi.ingsw.model.Color parsedColor = StudentAccessiblePiece.parseColor(inputColor);
            if(parsedColor == null){
                colorDialog();
            } else {
                setCharacterTargetColor(parsedColor);
            }
        });
    }

    /**
     * retrieves the asset matching the Color provided. Does not apply to Towers which have their own color enumeration.
     * @param color name of the color
     * @return the corresponding image to the color
     * @see Image
     * @see it.polimi.ingsw.model.Color
     */
    public Image getColorImage(it.polimi.ingsw.model.Color color){
        return new Image(getClass().getResourceAsStream("/assets/student_" + color.toString().toLowerCase(Locale.ROOT) + ".png"));
    }

    /**
     * retrieves the asset matching the TowerColor provided. Only applies for Towers.
     * @param towerColor name of the Tower's color
     * @return Image
     * @see TowerColor
     */
    public Image getColorImage(TowerColor towerColor) {
        return new Image(getClass().getResourceAsStream("/assets/tower_" + towerColor.toString().toLowerCase(Locale.ROOT) + ".png"));
    }

    /**
     * Renders a shadow of the Color provided
     * @param color the desired color for the shadow
     * @return a shadow effect, shaded with the provided color
     * @see Color
     */
    public DropShadow getHighlight(Color color){
        DropShadow effect = new DropShadow();
        effect.setHeight(40.0);
        effect.setWidth(40.0);
        effect.setRadius(20.0);
        effect.setSpread(0.65);
        effect.setColor(color);
        return effect;
    }

    /**
     * resets the variables that hold source location, target location and the student to move's id.
     * Used to perform Undo operations if the user chooses to do so.
     * @see #resetStatus()
     */
    public void studentActionReset(){
        studentToMove = null;
        studentSource = null;
        studentTarget = null;
    }

    /**
     * Resets the variables used to move Mother Nature across islands.
     * @see #resetStatus()
     */
    public void motherNatureActionReset(){
        originIslandIndex = 0;
        targetIslandIndex = 0;
    }

    /**
     * Resets the variables used to handle a Character.
     * Used to perform Undo operations if the user chooses to do so.
     * @see #resetStatus()
     */
    public void characterActionReset(){
        //Parameters
        currentCharacter = null;
        characterIndex = 0;
        characterTargetColor = null;
        characterStudentsInOrigin = new ArrayList<>();
        characterStudentsInTarget = new ArrayList<>();
        characterOriginPieces = new ArrayList<>();
        characterTargetPieces = new ArrayList<>();
        //Buttons
        colorBtnVisible = false;
        sourceStudentsBtnVisible = false;
        targetStudentsBtnVisible = false;
        targetPiecesBtnVisible = false;
        //Choosing
        choosingObject = NONE;
    }

    /**
     * Checks the current game status and chooses the appropriate reset method.
     * @see #status
     * @see #motherNatureActionReset()
     * @see #motherNatureActionReset()
     * @see #characterActionReset()
     */
    public void resetStatus(){
        switch (status) {
            case STUDENT -> studentActionReset();
            case MOTHER_NATURE -> motherNatureActionReset();
            case CHARACTER -> characterActionReset();
        }
        status = NONE;
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

    /**
     * Returns the Controller which has been mapped to the String provided, each implementing the GUIController interface.
     * Each Controller is in charge of handling specific aspects on the game, thus it's required to get the appropriate
     * one to perform a certain operation
     * @param name the String to search the HashMap for the desired controller
     * @return the Controller requested
     * @see GUIController
     * @see #nameMapController
     */
    public GUIController getControllerFromName(String name) {
        return nameMapController.get(name);
    }

    /**
     * @return ready
     */
    public boolean isReady() {
        return ready;
    }

    //STATUS

    /**
     * @param status sets the game status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the current game's status
     */
    public String getStatus() {
        return status;
    }

    //STUDENTS MOVEMENT

    /**
     * @param studentToMove the Student to move's id
     */
    public void setStudentToMove(Integer studentToMove) {
        this.studentToMove = studentToMove;
    }

    /**
     * @param studentSource the Student to move's source location
     */
    public void setStudentSource(Integer studentSource) {
        this.studentSource = studentSource;
    }

    /**
     * @param studentTarget the Student to move's target location
     */
    public void setStudentTarget(Integer studentTarget) {
        this.studentTarget = studentTarget;
    }

    /**
     * @return the Student to move's id
     */
    public Integer getStudentToMove() {
        return studentToMove;
    }

    /**
     * @return the Student to move's target location
     */
    public Integer getStudentSource() {
        return studentSource;
    }

    /**
     * @return the Student to move's target location
     */
    public Integer getStudentTarget() {
        return studentTarget;
    }

    //MOTHER NATURE

    /**
     * @return Mother Nature's starting Island's id
     */
    public Integer getOriginIslandIndex() {
        return originIslandIndex;
    }

    /**
     * @return Mother Nature's target Island's id
     */
    public Integer getTargetIslandIndex() {
        return targetIslandIndex;
    }

    /**
     * @param originIslandIndex Mother Nature's starting Island's id
     */
    public void setOriginIslandIndex(Integer originIslandIndex) {
        this.originIslandIndex = originIslandIndex;
    }

    /**
     * @param targetIslandIndex Mother Nature's target Island's id
     */
    public void setTargetIslandIndex(Integer targetIslandIndex) {
        this.targetIslandIndex = targetIslandIndex;
    }

    //CHARACTERS

    /**
     * @param characterIndex sets the Character's index
     */
    public void setCharacterIndex(int characterIndex) {
        this.characterIndex = characterIndex;
    }

    /**
     * @param characterTargetColor sets the Character's color of effect
     */
    public void setCharacterTargetColor(it.polimi.ingsw.model.Color characterTargetColor) {
        this.characterTargetColor = characterTargetColor;
    }

    /**
     * @param colorBtnVisible sets the visibility of the "Choose color" button in the Characters scene
     */
    public void setColorBtnVisible(boolean colorBtnVisible) {
        this.colorBtnVisible = colorBtnVisible;
    }

    /**
     * @param sourceStudentsBtnVisible sets the visibility of the "Choose source students" button in the Characters scene
     */
    public void setSourceStudentsBtnVisible(boolean sourceStudentsBtnVisible) {
        this.sourceStudentsBtnVisible = sourceStudentsBtnVisible;
    }

    /**
     * @param targetStudentsBtnVisible sets the visibility of the "Choose target students" button in the Characters scene
     */
    public void setTargetStudentsBtnVisible(boolean targetStudentsBtnVisible) {
        this.targetStudentsBtnVisible = targetStudentsBtnVisible;
    }

    /**
     * @param targetPiecesBtnVisible sets the visibility of the "Choose target pieces" button in the Characters scene
     */
    public void setTargetPiecesBtnVisible(boolean targetPiecesBtnVisible) {
        this.targetPiecesBtnVisible = targetPiecesBtnVisible;
    }

    /**
     * @return a List with the boolean values that control visibility for the "Choose color", "Choose source students",
     * "Choose target students" and "Choose target pieces" in the Characters scene
     */
    public List<Boolean> listOfCharacterButtons(){
        return new ArrayList<>(){
            {
                add(colorBtnVisible);
                add(sourceStudentsBtnVisible);
                add(targetStudentsBtnVisible);
                add(targetPiecesBtnVisible);
            }
        };
    }

    /**
     * @see PlayCharacterRequest
     * @return a new PlayCharacterRequest with gui parameters
     */
    public PlayCharacterRequest getCharacterRequest(){
        return new PlayCharacterRequest(
                characterIndex,
                clientController.getPlayerInfo().getNickname(),
                characterTargetColor,
                characterStudentsInOrigin,
                characterStudentsInTarget,
                characterOriginPieces,
                characterTargetPieces);
    }

    /**
     * @param choosingObject the object to handle that has been clicked by the user
     */
    public void setChoosingObject(String choosingObject) {
        this.choosingObject = choosingObject;
    }

    /**
     * @return the object to handle that has been clicked by the user
     */
    public String getChoosingObject() {
        return choosingObject;
    }

    /**
     * adds a Student to the location specified by the choosingObject string
     *
     * @param student the id of Student to add
     * @see #choosingObject
     * @see #setChoosingObject(String)
     */
    public void addStudent(Integer student){
        if(choosingObject.equals(GUI.STUDENTSINORIGIN)){
            //Add in origin/source
            characterStudentsInOrigin.add(student);
        } else if(choosingObject.equals(GUI.STUDENTSINTARGET)){
            //Add in target
            characterStudentsInTarget.add(student);
        }
    }

    /**
     * adds a piece to the location specified by the choosingObject string, used by certain Characters' effects
     *
     * @param piece the id of the piece add
     * @see #choosingObject
     * @see #setChoosingObject(String)
     */
    public void addPiece(Integer piece){
        if(choosingObject.equals(GUI.TARGET) || choosingObject.equals(GUI.STUDENTSINTARGET)){
            //Add in target
            characterTargetPieces.add(piece);
        } else {
            //Add in origin/source
            characterOriginPieces.add(piece);
        }
    }

    /**
     * @param currentCharacter sets the currently active Character
     */
    public void setCurrentCharacter(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    /**
     * @return the Character currently in effect
     */
    public Character getCurrentCharacter() {
        return currentCharacter;
    }
}