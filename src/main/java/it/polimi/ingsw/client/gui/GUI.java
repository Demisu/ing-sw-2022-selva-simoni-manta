package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.gui.controllers.GUIController;
import it.polimi.ingsw.client.gui.controllers.RealmController;
import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.StudentAccessiblePiece;
import it.polimi.ingsw.model.TowerColor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.security.cert.CertificateParsingException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;

public class GUI extends Application implements ClientView {

    //public static final String END_OF_THE_GAME = "End of the game";
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

    //Status for actions
    public static final String NONE = "NONE";
    public static final String MOTHER_NATURE = "MOTHER_NATURE";
    public static final String STUDENT = "STUDENT";
    public static final String CHARACTER = "CHARACTER";
    public static final String STUDENTSINORIGIN = "STUDENTSINORIGIN";
    public static final String STUDENTSINTARGET = "STUDENTSINTARGET";
    public static final String TARGET = "TARGET";

    //Status
    private String status = NONE;

    private boolean ready;

    //Used for student movement
    private Integer studentToMove;
    private Integer studentSource;
    private Integer studentTarget;

    //Variables for mother nature movement
    Integer originIslandIndex;
    Integer targetIslandIndex;

    //Variables for character buttons visibility
    private Boolean colorBtnVisible = false;
    private Boolean sourceStudentsBtnVisible = false;
    private Boolean targetStudentsBtnVisible = false;
    private Boolean targetPiecesBtnVisible = false;
    //Variables for character requests
    Character currentCharacter = null;
    int characterIndex = 0;
    it.polimi.ingsw.model.Color characterTargetColor = null;
    List<Integer> characterStudentsInOrigin = new ArrayList<>();
    List<Integer> characterStudentsInTarget = new ArrayList<>();
    List<Integer> characterOriginPieces = new ArrayList<>();
    List<Integer> characterTargetPieces = new ArrayList<>();
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
            main(null);
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

        updater = Executors.newSingleThreadScheduledExecutor();
        updater.scheduleAtFixedRate(this::reloadScene, 0, 1, TimeUnit.SECONDS);
    }

    public MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public void reloadScene(){
        //Update the scene only when needed
        if(currentScene.equals(nameMapScene.get(LOBBY))
                || currentScene.equals(nameMapScene.get(REALM))
                || currentScene.equals(nameMapScene.get(PROFILES))
                || currentScene.equals(nameMapScene.get(ASSISTANTS)) ) {
            Platform.runLater(() -> {
                changeScene(sceneMapName.get(currentScene));
                this.getControllerFromName(sceneMapName.get(currentScene)).onLoad();
            });
        }
    }

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

    public void createModal(Stage stage, String title, String asset, Color color, ArrayList<Text> info){
        final Stage dialog = new Stage();
        Pane bagRoot = new Pane();
        StackPane bagHolder = new StackPane();
        Canvas canvas = new Canvas(2000,2000);
        bagHolder.getChildren().add(canvas);
        bagRoot.getChildren().add(bagHolder);
        Scene bagScene = new Scene(bagRoot, 600, 400);
        dialog.setScene(bagScene);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("/assets/"+asset)));
        dialog.setTitle(title);
        TextFlow dialogTflow = new TextFlow();
        dialogTflow.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        dialogTflow.getChildren().addAll(info);
        Scene dialogScene = new Scene(dialogTflow, 300, 200);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    /**
     * Shows a dialog window to select a color
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

    public Image getColorImage(it.polimi.ingsw.model.Color color){
        return new Image(getClass().getResourceAsStream("/assets/student_" + color.toString().toLowerCase(Locale.ROOT) + ".png"));
    }

    public Image getColorImage(TowerColor towerColor) {
        return new Image(getClass().getResourceAsStream("/assets/tower_" + towerColor.toString().toLowerCase(Locale.ROOT) + ".png"));
    }

    public DropShadow getHighlight(Color color){
        DropShadow effect = new DropShadow();
        effect.setHeight(40.0);
        effect.setWidth(40.0);
        effect.setRadius(20.0);
        effect.setSpread(0.65);
        effect.setColor(color);
        return effect;
    }

    public void studentActionReset(){
        studentToMove = null;
        studentSource = null;
        studentTarget = null;
    }

    public void motherNatureActionReset(){
        originIslandIndex = 0;
        targetIslandIndex = 0;
    }

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
    public GUIController getControllerFromName(String name) {
        return nameMapController.get(name);
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public boolean isReady() {
        return ready;
    }

    //STATUS

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    //STUDENTS MOVEMENT

    public void setStudentToMove(Integer studentToMove) {
        this.studentToMove = studentToMove;
    }

    public void setStudentSource(Integer studentSource) {
        this.studentSource = studentSource;
    }

    public void setStudentTarget(Integer studentTarget) {
        this.studentTarget = studentTarget;
    }

    public Integer getStudentToMove() {
        return studentToMove;
    }

    public Integer getStudentSource() {
        return studentSource;
    }

    public Integer getStudentTarget() {
        return studentTarget;
    }

    //MOTHER NATURE

    public Integer getOriginIslandIndex() {
        return originIslandIndex;
    }

    public Integer getTargetIslandIndex() {
        return targetIslandIndex;
    }

    public void setOriginIslandIndex(Integer originIslandIndex) {
        this.originIslandIndex = originIslandIndex;
    }

    public void setTargetIslandIndex(Integer targetIslandIndex) {
        this.targetIslandIndex = targetIslandIndex;
    }

    //CHARACTERS

    public int getCharacterIndex() {
        return characterIndex;
    }

    public void setCharacterIndex(int characterIndex) {
        this.characterIndex = characterIndex;
    }

    public it.polimi.ingsw.model.Color getCharacterTargetColor() {
        return characterTargetColor;
    }

    public void setCharacterTargetColor(it.polimi.ingsw.model.Color characterTargetColor) {
        this.characterTargetColor = characterTargetColor;
    }

    public List<Integer> getCharacterStudentsInOrigin() {
        return characterStudentsInOrigin;
    }

    public void addCharacterStudentsInOrigin(Integer characterStudentsInOrigin) {
        this.characterStudentsInOrigin.add(characterStudentsInOrigin);
    }

    public List<Integer> getCharacterStudentsInTarget() {
        return characterStudentsInTarget;
    }

    public void addCharacterStudentsInTarget(Integer characterStudentsInTarget) {
        this.characterStudentsInTarget.add(characterStudentsInTarget);
    }

    public List<Integer> getCharacterOriginPieces() {
        return characterOriginPieces;
    }

    public void addCharacterOriginPieces(Integer characterOriginPieces) {
        this.characterOriginPieces.add(characterOriginPieces);
    }

    public List<Integer> getCharacterTargetPieces() {
        return characterTargetPieces;
    }

    public void addCharacterTargetPieces(Integer characterTargetPieces) {
        this.characterTargetPieces.add(characterTargetPieces);
    }

    public Boolean isColorBtnVisible() {
        return colorBtnVisible;
    }

    public void setColorBtnVisible(boolean colorBtnVisible) {
        this.colorBtnVisible = colorBtnVisible;
    }

    public Boolean isSourceStudentsBtnVisible() {
        return sourceStudentsBtnVisible;
    }

    public void setSourceStudentsBtnVisible(boolean sourceStudentsBtnVisible) {
        this.sourceStudentsBtnVisible = sourceStudentsBtnVisible;
    }

    public Boolean isTargetStudentsBtnVisible() {
        return targetStudentsBtnVisible;
    }

    public void setTargetStudentsBtnVisible(boolean targetStudentsBtnVisible) {
        this.targetStudentsBtnVisible = targetStudentsBtnVisible;
    }

    public Boolean isTargetPiecesBtnVisible() {
        return targetPiecesBtnVisible;
    }

    public void setTargetPiecesBtnVisible(boolean targetPiecesBtnVisible) {
        this.targetPiecesBtnVisible = targetPiecesBtnVisible;
    }

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

    public void setChoosingObject(String choosingObject) {
        this.choosingObject = choosingObject;
    }

    public String getChoosingObject() {
        return choosingObject;
    }

    public void addStudent(Integer student){
        if(choosingObject.equals(GUI.STUDENTSINORIGIN)){
            //Add in origin/source
            characterStudentsInOrigin.add(student);
        } else if(choosingObject.equals(GUI.STUDENTSINTARGET)){
            //Add in target
            characterStudentsInTarget.add(student);
        }
    }

    public void addPiece(Integer piece){
        if(choosingObject.equals(GUI.TARGET) || choosingObject.equals(GUI.STUDENTSINTARGET)){
            //Add in target
            characterTargetPieces.add(piece);
        } else {
            //Add in origin/source
            characterOriginPieces.add(piece);
        }
    }

    public void setCurrentCharacter(Character currentCharacter) {
        this.currentCharacter = currentCharacter;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }
}