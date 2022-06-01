package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static it.polimi.ingsw.model.StudentAccessiblePiece.colorOfStudent;
import static it.polimi.ingsw.model.StudentAccessiblePiece.indexOfColor;

public class CLI implements ClientView {

    Scanner scanner = new Scanner(System.in);
    String nickname;
    int movedStudents;
    int studentsToMove;

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

    private final ClientController clientController;

    public CLI(ClientController clientController) {
        this.clientController = clientController;
    }

    public void waitGameStartPhase() {

        Boolean status = false;
        System.out.println("Waiting for all players...");
        while(!status){
            status = clientController.waitGameStart();
        }

    }

    public void setupPhase() {

        Boolean choosePlayersNumber;
        Boolean status = false;

        do {
            System.out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
        } while (nickname == null);

        choosePlayersNumber = clientController.setPlayerNickname(nickname);

        if(choosePlayersNumber){
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
            status = clientController.setPlayerNumber(number, expertMode);
        }
        if(!status){
            System.out.println("""
                    Game already playing, added to lobby.
                    The game will start as soon as all players have joined.
                    """);
        }
    }

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
                break;
            }

            showAvailableActions();
            System.out.println("Your choice: ");
            String action = scanner.nextLine();

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {

                clearConsole();
                printLongRow();
                System.out.println("* GAME PHASE HAS CHANGED *");
                System.out.println("moving to Action phase...");
                printLongRow();
                break;
            }

            processAction(action);

        } while(clientController.getGamePhase().equals(GamePhase.PLANNING));
    }

    public void actionPhase() {

        availableActions = turnActions;
        studentsToMove = clientController.getGameInfo().getPlayers().size() == 3 ? 4 : 3;

        System.out.println("Action phase has started!");
        String action;

        do {

            clientController.getModelInfo();

            if(!clientController.getGamePhase().equals(GamePhase.ACTION)) {
                break;
            }

            showAvailableActions();
            System.out.println("Your choice: ");
            action = scanner.nextLine();
            processAction(action);

        } while(clientController.getGamePhase().equals(GamePhase.ACTION));

        Boolean status = clientController.passTurn();
    }

    public void processAction(String action){

        if(!availableActions.contains(action)){
            System.out.println("Invalid action.");
        } else {
            switch(action){

                case playCharacterAction -> {

                    showCharacters();
                    System.out.println("Input character number: ");
                    Integer characterNumber = scanner.nextInt();

                    scanner.nextLine();
                    clientController.playCharacter(characterNumber);
                }

                case playAssistantAction -> {

                    showAssistants();
                    System.out.println("Input assistant number: ");
                    Integer assistantNumber = scanner.nextInt();

                    scanner.nextLine();
                    clientController.playAssistant(assistantNumber);
                }

                case moveStudentAction -> {

                    System.out.println("Input student ID: ");
                    Integer student = scanner.nextInt();
                    System.out.println("Input source ID: ");
                    Integer source = scanner.nextInt();
                    System.out.println("Input target ID: ");
                    Integer target = scanner.nextInt();

                    scanner.nextLine();
                    clientController.moveStudent(student, source, target);
                    //after moving n students, mother nature can be moved
                    if(movedStudents == studentsToMove) {
                        availableActions.add(moveMotherNatureAction);
                    }
                }

                case moveMotherNatureAction -> {

                    System.out.println("Input movements: ");
                    Integer steps = scanner.nextInt();

                    scanner.nextLine();
                    clientController.moveMotherNature(steps);
                    //Once mother nature is moved, the action cannot be repeated
                    availableActions.remove(moveMotherNatureAction);
                    //Adds turn-ending action, equivalent to "pass turn"
                    availableActions.add(chooseCloudAction);
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

                    if(infoNumber >= availableInfo.size()){
                        System.out.println("Invalid number");
                    } else {
                        switch (availableInfo.get(infoNumber)) {
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

    public void showClouds(){
        clearConsole();
        printLongRow();
        System.out.println("Available clouds:");
        for (Cloud cloud : clientController.getClouds()){
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

    public void showCharacters() {
        clearConsole();
        printLongRow();
        System.out.println("Available characters:");
        System.out.println("(index: id | cost)");
        printShortRow();
        ArrayList<Character> characters = clientController.getCharacters();
        for (Character character : characters) {
            if(!character.getHasBeenUsed()) {
                System.out.println(characters.indexOf(character) + ":\t"
                        + character.getImage() + "\t|\t"
                        + character.getCost());
            }
        }
        printLongRow();
    }

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

    public void showSchoolBoards() {
        clearConsole();
        printLongRow();
        System.out.println("School Boards:");
        for (Player player : clientController.getGameInfo().getPlayers()){
            printShortRow();
            SchoolBoard schoolBoard = player.getPlayerBoard();
            System.out.println("Player: " + player.getNickname());
            //Entrance
            System.out.println("Students in entrance: " + schoolBoard.getStudents().size());
            System.out.print("Colors: ");
            schoolBoard.getStudents().forEach(student -> {
                System.out.print(colorOfStudent(student));
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

    public void showAvailableActions() {
        printLongRow();
        System.out.println("Allowed actions:");
        printShortRow();
        availableActions.forEach(System.out::println);
        printLongRow();
    }

    // UTILITY

    public void printLongRow(){
        System.out.println("------------------------------------------------");
    }

    public void printShortRow(){
        System.out.println("----------------");
    }

    public void clearConsole(){
        for (int i = 0; i < 30; i++) {
            System.out.println();
        }
    }

    public void testingPhase () {

        /*Scanner scanner = new Scanner(System.in);
        String action;

        do {

            System.out.println("Waiting your turn...");
            clientController.getModelInfo();
            firstRequest = false;

            System.out.println("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    PLAY_ASSISTANT
                    PLAY_CHARACTER
                    MOVE_MOTHERNATURE
                    MOVE_STUDENT
                    PASS_TURN
                    -----------------
                    """);
            System.out.println("Your choice: ");
            action = scanner.nextLine();

            switch (action) {
                case "PLAY_ASSISTANT" -> {
                    System.out.println("""
                            -----------------
                            Available assistants:\s
                            (format: turnPriority | motherMovs)
                            -----------------
                            """);
                    List<Assistant> deck = clientController.getPlayerInfo().getDeck();
                    for (Assistant assistant : deck) {
                        System.out.println(deck.indexOf(assistant) + ": "
                                + assistant.getTurnPriority()+ " | "
                                + assistant.getMotherNatureMovements());
                    }
                    System.out.println("\nInput assistant number: ");
                    clientController.playAssistant(scanner.nextInt());
                    scanner.nextLine();
                }
                case "PLAY_CHARACTER" -> {
                    System.out.println("\nInput character number: ");
                    clientController.playCharacter(scanner.nextInt());
                    scanner.nextLine();
                }
                case "MOVE_MOTHERNATURE" -> {
                    System.out.println("\nInput movements: ");
                    clientController.moveMotherNature(scanner.nextInt());
                    scanner.nextLine();
                }
                case "MOVE_STUDENT" -> {
                    System.out.println("\nInput student ID: ");
                    Integer student = scanner.nextInt();
                    System.out.println("\nInput source ID: ");
                    Integer source = scanner.nextInt();
                    System.out.println("\nInput target ID: ");
                    Integer target = scanner.nextInt();
                    clientController.moveStudent(student, source, target);
                    scanner.nextLine();
                }
                case "PASS_TURN" -> {
                }
                default -> System.out.println("Invalid action.");
            }
        } while(!action.equals("PASS_TURN"));

        Boolean status = clientController.passTurn();*/
    }
}
