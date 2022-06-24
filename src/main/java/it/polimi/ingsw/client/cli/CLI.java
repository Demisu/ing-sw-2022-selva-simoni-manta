package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.client.requests.PlayCharacterRequest;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.model.Color.*;
import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

/**
 * CLI class for Command Line Interface game UI
 */
public class CLI implements ClientView {

    private ScheduledExecutorService updater;

    Scanner scanner = new Scanner(System.in);
    String nickname;
    int movedStudents;

    final String playCharacterAction = "PLAY CHARACTER";
    final String playAssistantAction = "PLAY ASSISTANT";
    final String moveStudentAction = "MOVE STUDENT";
    final String moveMotherNatureAction = "MOVE MOTHERNATURE";
    final String chooseCloudAction = "CHOOSE CLOUD";
    final String infoAction = "INFO";
    final String quitAction = "QUIT";

    final String infoISLANDS = "ISLANDS";
    final String infoCLOUDS = "CLOUDS";
    final String infoCHARACTERS = "CHARACTERS";
    final String infoASSISTANTS = "ASSISTANTS";
    final String infoSCHOOLBOARDS = "SCHOOL BOARDS";
    final String infoBAG = "BAG";

    ArrayList<String> availableActions = new ArrayList<>();
    ArrayList<String> planningActions = new ArrayList<>(){
        {
            add(playAssistantAction);
            add(infoAction);
            add(quitAction);
        }
    };
    ArrayList<String> turnActions = new ArrayList<>(){
        {
            add(playCharacterAction);
            add(moveStudentAction);
            add(infoAction);
            add(quitAction);
        }
    };
    ArrayList<String> availableInfo = new ArrayList<>(){
        {
            add(infoISLANDS);
            add(infoCLOUDS);
            add(infoCHARACTERS);
            add(infoASSISTANTS);
            add(infoSCHOOLBOARDS);
            add(infoBAG);
        }
    };

    private ClientController clientController;

    /**
     * general constructor for CLI instances
     *
     * @param clientController controller for the CLI
     */
    public CLI(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Setup phase handler, prints all necessary info and available commands for the setup of the game
     */
    public void setupPhase() {

        Integer result;

        do {
            System.out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
        } while (nickname == null);

        result = clientController.setPlayerNickname(nickname);

        switch(result){
            case 0 -> {
                System.out.println("""
                    Game already playing, added to lobby.
                    The game will start as soon as all players have joined.
                    """);
            }
            case 1 -> {
                int number;
                do {
                    System.out.println("Choose player number:\n[ 2 / 3 / 4 ]");
                    number = Integer.parseInt(scanner.nextLine());
                } while (number > 4 || number < 2);
                System.out.println("Expert mode?\n[ true (1) / false (2) ]");
                String answer = scanner.nextLine();
                if(answer.equals("1")) {
                    answer = "true";
                }
                Boolean expertMode = Boolean.parseBoolean(answer);
                clientController.setPlayerNumber(number, expertMode);
            }
            case 2 -> {
                System.out.println("The game is full, quitting...");
                clientController.closeConnection();
                System.exit(0);
            }
        }

        updater = Executors.newSingleThreadScheduledExecutor();
        updater.scheduleAtFixedRate(() -> {
            clientController.getModelInfo();
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * @param clientController controller for the CLI
     */
    @Override
    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    /**
     * Wait end of setup phase handler, makes the player wait the rest of the players
     */
    public void waitGameStartPhase() {

        Boolean status = false;
        System.out.println("Waiting for all players...");
        while(!status){
            status = clientController.waitGameStart();
        }

    }

    /**
     * Planning phase handler, prints all necessary info and available commands for the setup of the game.
     * Handles assistant use and main game info
     */
    public void planningPhase() {

        availableActions = planningActions;
        movedStudents = 0;

        do {

            clientController.getModelInfo();
            //If expert mode is off, don't display character actions
            if(!clientController.getGameInfo().isExpertMode()){
                availableActions.remove(playCharacterAction);
                availableInfo.remove(infoCHARACTERS);
            }
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {
                if (clientController.getGamePhase().equals(GamePhase.END)) {
                    endingPhase();
                }
                break;
            }

            showAvailableActions();
            System.out.println("Your choice: ");
            String action = scanner.nextLine();

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {
                if (clientController.getGamePhase().equals(GamePhase.END)) {
                    endingPhase();
                }
                clearConsole();
                printLongRow();
                System.out.println("* GAME PHASE HAS CHANGED *");
                System.out.println("moving to Action phase...");
                printLongRow();
                break;
            }

            parseAction(action);

        } while(clientController.getGamePhase().equals(GamePhase.PLANNING));
    }

    /**
     * Action phase handler, prints all necessary info and available commands for the setup of the game.
     * Handles character use, student movement, cloud selection, mother nature movement and main game info
     */
    public void actionPhase() {

        availableActions = turnActions;

        System.out.println("Action phase has started!");
        String action;

        do {

            clientController.getModelInfo();

            if (!clientController.getGamePhase().equals(GamePhase.ACTION)) {
                if (clientController.getGamePhase().equals(GamePhase.END)) {
                    endingPhase();
                }
                break;
            }

            showAvailableActions();
            System.out.println("Your choice: ");
            action = scanner.nextLine();
            parseAction(action);

        } while(clientController.getGamePhase().equals(GamePhase.ACTION));
    }

    /**
     * Ending phase handler, notifies the player that the game ended and shows the winner
     */
    public void endingPhase() {

        clientController.getModelInfo();
        Team winner = clientController.getGameInfo().getWinnerTeam();

        System.out.println("*** THE GAME HAS ENDED ***");
        System.out.print("WINNER: Team " + (winner.getTeamId() + 1) + " [ ");
        for (Player player : winner.getPlayers()){
            System.out.print(player.getNickname() + " ");
        }
        System.out.println("]");

        System.out.println("Press any key to exit");
        scanner.nextLine();
        System.exit(0);
    }

    /**
     * Parses the input string and interprets it as an action. If the action is badly typed or not available, prompts
     * the user to enter another one.
     *
     * @param action string of the action input
     */
    public void parseAction(String action){

        if(!availableActions.contains(action)){
            System.out.println("Invalid action.");
        } else {
            clientController.getModelInfo();
            switch(action){

                case playCharacterAction -> {

                    showCharacters();
                    System.out.println("Input character number: ");
                    Integer characterNumber = scanner.nextInt();

                    scanner.nextLine();

                    //req = new PlayCharacterRequest(characterNumber, nickname, targetColor, studentsInOrigin, studentsInTarget, originPieces, targetPieces);
                    clientController.playCharacter(generateCharacterRequest(characterNumber));
                }

                case playAssistantAction -> {

                    showAssistants();
                    System.out.println("Input assistant number: ");
                    Integer assistantNumber = scanner.nextInt();

                    scanner.nextLine();
                    clientController.playAssistant(assistantNumber);
                }

                case moveStudentAction -> {

                    int student;
                    int source;
                    int target;

                    showIslands();
                    showSchoolBoards();

                    do {
                        System.out.println("Input student ID: ");
                        student = scanner.nextInt();
                        scanner.nextLine();
                    } while(student < 0 || student > 130);
                    do {
                        System.out.println("Input source ID: ");
                        source = scanner.nextInt();
                        scanner.nextLine();
                    } while(source < 0);
                    do {
                        System.out.println("Input target ID: ");
                        target = scanner.nextInt();
                        scanner.nextLine();
                    } while(target < 0);

                    Boolean success = clientController.moveStudent(student, source, target);
                    if(success) {
                        movedStudents++;
                        //after moving n students, mother nature can be moved
                        if (movedStudents == clientController.getGameInfo().getStudentsToMove()) {
                            availableActions.remove(moveStudentAction);
                            //Removes these to avoid adding cloud in the last spot
                            availableActions.remove(infoAction);
                            availableActions.remove(quitAction);
                            availableActions.add(moveMotherNatureAction);
                            //Add them again
                            availableActions.add(infoAction);
                            availableActions.add(quitAction);
                        }
                    } else {
                        System.out.println("Error moving student, please try again");
                    }
                }

                case moveMotherNatureAction -> {

                    int steps;
                    do {
                        System.out.println("Input movements: ");
                        steps = scanner.nextInt();
                        scanner.nextLine();
                    }while(steps <= 0 || steps > clientController.getPlayerInfo().getLastAssistantPlayed().getMotherNatureMovements());

                    clientController.moveMotherNature(steps);
                    //Once mother nature is moved, the action cannot be repeated
                    availableActions.remove(moveMotherNatureAction);
                    //Removes these to avoid adding cloud in the last spot
                    availableActions.remove(infoAction);
                    availableActions.remove(quitAction);
                    //Adds turn-ending action, equivalent to "pass turn"
                    availableActions.add(chooseCloudAction);
                    //Add them again
                    availableActions.add(infoAction);
                    availableActions.add(quitAction);
                }

                case chooseCloudAction -> {

                    showClouds();
                    System.out.println("Choose cloud: ");
                    Integer cloud = scanner.nextInt();

                    scanner.nextLine();
                    Set<Integer> studentsFromCloud = clientController.getGameInfo().getStudentAccessiblePieceByID(cloud).getStudents();
                    Integer targetBoard = clientController.getPlayerInfo().getPlayerBoard().getPieceID();
                    //Move each student to player's school board
                    studentsFromCloud.forEach(student -> clientController.moveStudent(student, cloud, targetBoard));
                    //Last action, player's turn has ended
                    clientController.passTurn();
                }

                case infoAction -> {

                    showInfo();

                    System.out.println("Input info number: ");
                    int infoNumber = scanner.nextInt();

                    scanner.nextLine();

                    if(infoNumber > availableInfo.size()){
                        System.out.println("Invalid number");
                    } else {
                        switch (availableInfo.get(infoNumber - 1)) {
                            case infoISLANDS -> {
                                showIslands();
                            }
                            case infoCLOUDS -> {
                                showClouds();
                            }
                            case infoCHARACTERS -> {
                                showCharacters();
                            }
                            case infoASSISTANTS -> {
                                showAssistants();
                            }
                            case infoSCHOOLBOARDS -> {
                                showSchoolBoards();
                            }
                            case infoBAG -> {
                                showBagStudents();
                            }
                        }
                    }
                }

                case quitAction -> {
                    System.out.println("Quitting...");
                    clientController.closeConnection();
                }
            }
        }
    }

    /**
     * Printer method to show clouds status
     */
    public void showClouds(){
        clearConsole();
        printLongRow();
        System.out.println("Available clouds:");
        for (Cloud cloud : clientController.getClouds()){
            //Don't print empty clouds
            if(cloud.getStudents().size() == 0){
                continue;
            }
            printShortRow();
            System.out.println("ID: " + cloud.getPieceID());
            System.out.print("Students: " + cloud.getStudents().size() + " [ ");
            cloud.getStudents().forEach(student -> {
                System.out.print(colorOfStudent(student));
                System.out.print(" ");
            });
            System.out.println("]");
        }
        printLongRow();
    }

    /**
     * Printer method to show assistants status
     */
    public void showAssistants() {
        clearConsole();
        printLongRow();
        System.out.println("Available assistants:");
        System.out.println("(index: turnPriority | motherMovs)");
        printShortRow();
        List<Assistant> deck = clientController.getPlayerInfo().getDeck();
        for (Assistant assistant : deck) {
            System.out.println(deck.indexOf(assistant) + ":\t"
                    + assistant.getTurnPriority() + "\t|\t"
                    + assistant.getMotherNatureMovements());
        }
        printLongRow();
    }

    /**
     * Printer method to show characters status
     */
    public void showCharacters() {
        clearConsole();
        printLongRow();
        System.out.println("Available characters:");
        System.out.println("(index: id | cost | description)");
        printShortRow();
        ArrayList<Character> characters = clientController.getCharacters();
        for (Character character : characters) {
            if(!character.getHasBeenUsed()) {
                System.out.println(characters.indexOf(character) + ":\t"
                        + character.getImage() + "\t|\t"
                        + character.getCost() + "\t|\t"
                        + character.getEffectType() + " " + character.getEffectNumberMax() + " " + character.getEffectObject() + " "
                        + "from " + character.getEffectSource() + " to " + character.getEffectTarget());
            }
        }
        printLongRow();
    }

    /**
     * Printer method to show islands status
     */
    public void showIslands() {
        clearConsole();
        printLongRow();
        System.out.println("Islands:");
        for (Island island : clientController.getIslands()){
            printShortRow();
            System.out.println("ID: " + island.getPieceID());
            System.out.print("Students: " + island.getStudents().size() + " [ ");
            island.getStudents().forEach(student -> {
                System.out.print(colorOfStudent(student));
                System.out.print(" ");
            });
            System.out.println("]");
            if(island.getTowersColor() == null){
                System.out.println("Towers: None");
            } else {
                System.out.println("Towers: " + island.getTowersNumber() + " " + island.getTowersColor());
            }
            System.out.println("No entry: " + island.getNoEntry());
            System.out.println("Mother nature: " + (island.isMotherNature() ? "YES" : "no"));
        }
        printLongRow();
    }

    /**
     * Printer method to show schoolboards status
     */
    public void showSchoolBoards() {
        clearConsole();
        printLongRow();
        System.out.println("School Boards:");
        for (Player player : clientController.getGameInfo().getPlayers()) {
            printShortRow();
            SchoolBoard schoolBoard = player.getPlayerBoard();
            System.out.println("ID: " + schoolBoard.getPieceID());
            System.out.println("Player: " + player.getNickname()
                    + (clientController.getGameInfo().getCurrentPlayer().equals(player.getNickname()) ? " [CURRENT]" : ""
                    + (clientController.getPlayerInfo().getNickname().equals(player.getNickname()) ? " [YOU]" : "")));
            //Entrance
            System.out.println("Students in entrance: " + schoolBoard.getStudents().size());
            System.out.print("Colors: ");
            schoolBoard.getStudents().forEach(student -> {
                System.out.print(colorOfStudent(student));
                System.out.print("[" + student + "]");
                System.out.print(" ");
            });
            System.out.println();
            //Dining room
            System.out.println("Students in dining room:");
            for (Color color : Color.values()) {
                System.out.println(color + ":\t" + schoolBoard.getDiningRoomStudents(color) +
                        "\tProfessor: " + (schoolBoard.getProfessors()[indexOfColor(color)] ? "YES" : "no"));
            }
        }
        printLongRow();
    }

    /**
     * Printer method to show general info
     */
    public void showInfo(){
        clearConsole();
        printLongRow();
        System.out.println("Available info:");
        printShortRow();
        int i = 1;
        for(String info : availableInfo){
            System.out.println(i + ") " + info);
            i++;
        }
        printLongRow();
    }

    /**
     * Printer method to show game bag info
     */
    public void showBagStudents() {
        clearConsole();
        ArrayList<Integer> studentsInBag = clientController.getGameInfo().getBagStudents();
        printLongRow();
        System.out.println("Bag info:");
        printShortRow();
        System.out.println("Students remaining: " + studentsInBag.size());
        for (Color color : Color.values()) {
            System.out.println(color + ": "
                    + studentsInBag.stream().filter(s -> colorOfStudent(s).equals(color)).count()
                    + " students");
        }
        printLongRow();
    }

    /**
     * Printer method to show all available actions
     */
    public void showAvailableActions() {
        printLongRow();
        System.out.println("Allowed actions:");
        printShortRow();
        availableActions.forEach(System.out::println);
        printLongRow();
    }

    /**
     * Builder method to create a correct and functional request for a character effect. Handles character types and
     * automatically asks only strictly necessary parameters for the character usage.
     *
     * @param characterNumber number of the character to play
     * @return PlayCharacterRequest to send to the controller, to play the character
     */
    public PlayCharacterRequest generateCharacterRequest(Integer characterNumber){

        Character character = clientController.getCharacters().get(characterNumber);
        //REQUEST PARAMETERS
        Color targetColor = null;
        List<Integer> studentsInOrigin = new ArrayList<>();
        List<Integer> studentsInTarget = new ArrayList<>();
        List<Integer> originPieces = new ArrayList<>();
        List<Integer> targetPieces = new ArrayList<>();

        switch (character.getEffectType()) {

            case "move" -> {

                //Source
                if (character.getEffectSource().equals("character")) {
                    //from character [char 1, 11]
                    if (character.getSetupObject().equals("student")) {
                        //[char 1, 11]
                        System.out.print("Select a student: [ ");
                        for (Integer student : character.getStudents()){
                            System.out.print(student + " ");
                        }
                        System.out.println("] to move to " + character.getEffectTarget());
                        studentsInOrigin.add(scanner.nextInt());
                        scanner.nextLine();
                    }
                } else if(character.getEffectSource().equals("dining_room")) {
                    //[char 12]
                    System.out.println("Input target color:");
                    int i = 1;
                    for (Color color : Color.values()){
                        System.out.println(i + ") " + color);
                        i++;
                    }
                    String inputColor = scanner.nextLine();
                    targetColor = parseColor(inputColor);
                }

                //Target
                if (character.getEffectTarget().equals("island")) {
                    //[char 1]
                    showIslands();
                    System.out.println("Input target island id:");
                    targetPieces.add(scanner.nextInt());
                    scanner.nextLine();
                } else if (character.getEffectTarget().equals("dining_room")) {
                    //[char 11]
                    targetPieces.add(clientController.getPlayerInfo().getPlayerBoard().getPieceID());
                }
            }

            case "exchange" -> {

                int targetSelected = 0;

                if (character.getEffectObject().equals("student")) {
                    //[char 10]
                    if (character.getEffectSource().equals("entrance")) {
                        System.out.println("Choose up to " + character.getEffectNumberMax() + "students to exchange between "
                                + character.getEffectSource() + " and " + character.getEffectTarget());

                        for(int i = 0; i < character.getEffectNumberMax(); i++){
                            System.out.print("Select a student: [ ");
                            for (Integer student : clientController.getPlayerInfo().getPlayerBoard().getStudents()){
                                System.out.print(student + " ");
                            }
                            System.out.println("] from your schoolboard");
                            System.out.println("(input . to stop)");
                            String input = scanner.nextLine();
                            if(input.equals(".")){
                                break;
                            }
                            originPieces.add(clientController.getPlayerInfo().getPlayerBoard().getPieceID());
                            studentsInOrigin.add(Integer.parseInt(input));
                            targetSelected++;
                        }

                    } else if(character.getEffectSource().equals("character")) {
                        //[char 7]
                        for(int i = 0; i < character.getEffectNumberMax(); i++){
                            System.out.print("Select a student: [ ");
                            for (Integer student : character.getStudents()){
                                System.out.print(student + " ");
                            }
                            System.out.println("] from this card");
                            System.out.println("(input . to stop)");
                            String input = scanner.nextLine();
                            if(input.equals(".")){
                                break;
                            }
                            originPieces.add(character.getPieceID());
                            studentsInOrigin.add(Integer.parseInt(input));
                            targetSelected++;
                        }
                    }
                    if (character.getEffectTarget().equals("dining_room")) {
                        //[char 10]
                        //Select targetSelected students
                        for(int i = 0; i < targetSelected; i++){
                            System.out.print("Select a student: [ ");
                            for (Integer student : clientController.getPlayerInfo().getPlayerBoard().getStudents()){
                                System.out.print(student + " ");
                            }
                            System.out.println("] from your dining room");
                            String input = scanner.nextLine();
                            targetPieces.add(clientController.getPlayerInfo().getPlayerBoard().getPieceID());
                            studentsInTarget.add(Integer.parseInt(input));
                        }
                    } else if(character.getEffectTarget().equals("entrance")) {
                        //[char 7]
                        //Select targetSelected students
                        for(int i = 0; i < targetSelected; i++){
                            System.out.print("Select a student: [ ");
                            for (Integer student : clientController.getPlayerInfo().getPlayerBoard().getStudents()){
                                System.out.print(student + " ");
                            }
                            System.out.println("] from your entrance");
                            String input = scanner.nextLine();
                            targetPieces.add(clientController.getPlayerInfo().getPlayerBoard().getPieceID());
                            studentsInTarget.add(Integer.parseInt(input));
                        }
                    }
                }
            }
            case "add" -> {
                if (character.getEffectCondition().equals("any")) {
                    //If nothing more is needed [char 2, 4, 5, 6, 8]
                    if (character.getEffectTarget().equals("island")) {
                        //[char 5]
                        showIslands();
                        System.out.println("Input target island ID (adds 1 noEntry tile):");
                        targetPieces.add(scanner.nextInt());
                    } else {
                        //[char 2, 4, 6, 8]
                        System.out.println("Nothing more needed");
                    }
                } else if (character.getEffectCondition().equals("color")) {
                    //If color is needed [char 9]
                    System.out.println("Input target color:");
                    int i = 1;
                    for (Color color : Color.values()){
                        System.out.println(i + ") " + color);
                        i++;
                    }
                    String inputColor = scanner.nextLine();
                    targetColor = parseColor(inputColor);
                }
            }
            case "resolve" -> {
                if (character.getEffectTarget().equals("island")) {
                    //[char 3]
                    showIslands();
                    System.out.println("Input island ID to resolve:");
                    targetPieces.add(scanner.nextInt());
                }
            }
        }

        return new PlayCharacterRequest(characterNumber, nickname, targetColor, studentsInOrigin, studentsInTarget, originPieces, targetPieces);
    }

    // UTILITY

    /**
     * Utility method to print a long line
     */
    public void printLongRow(){
        System.out.println("------------------------------------------------");
    }

    /**
     * Utility method to print a short line
     */
    public void printShortRow(){
        System.out.println("----------------");
    }

    /**
     * Utility method to clear the CLI
     */
    public void clearConsole(){
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    /**
     * @param color color to parse
     * @return Color equivalent to input string, null if not found
     */
    public Color parseColor(String color){
        switch (color) {
            case "YELLOW", "1" -> {return YELLOW;}
            case "BLUE", "2" -> {return BLUE;}
            case "GREEN", "3" -> {return GREEN;}
            case "RED", "4" -> {return RED;}
            case "PURPLE", "5" -> {return PURPLE;}
        }
        return null;
    }
}
