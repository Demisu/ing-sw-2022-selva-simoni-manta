package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CLI implements ClientView {

    Scanner scanner = new Scanner(System.in);
    String nickname;

    final String playCharacterAction = "PLAY_CHARACTER";
    final String playAssistantAction = "PLAY_ASSISTANT";
    final String moveStudentAction = "MOVE_STUDENT";
    final String moveMotherNatureAction = "MOVE_MOTHERNATURE";
    final String chooseCloudAction = "CHOOSE_CLOUD";
    final String infoAction = "INFO";
    final String quitAction = "QUIT";
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
            add(chooseCloudAction); //TO BE REMOVED !!!!!!!!!!!!!!!!!!!!
            add(infoAction);
            add(quitAction);
        }
    };

    /**
     * holds a reference to the Client's Controller
     */

    private final ClientController clientController;

    public CLI(ClientController clientController) {
        this.clientController = clientController;
    }

    public void waitGameStartPhase() {

        Boolean status = false;

        //System.out.println("Waiting further implementations");
        //Scanner scanner = new Scanner(System.in);
        //String placeholder = scanner.nextLine();

        //TO CHECK
        System.out.println("Waiting for all players...");
        while(!status){
            status = clientController.waitGameStart();
        }

    }

    public void setupPhase() {

        Boolean choosePlayersNumber = false;
        Boolean status = false;

        do {
            System.out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
        } while (nickname == null);

        choosePlayersNumber = clientController.setPlayerNickname(nickname);

        if(choosePlayersNumber){
            Integer number;
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
        if(status){
            //System.out.println("Game Created");
        } else {
            System.out.println("""
                    Game already playing, added to lobby.
                    The game will start as soon as all players have joined.
                    """);
        }
    }

    public void planningPhase() {

        availableActions = planningActions;

        do {

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {
                break;
            }

            System.out.print("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    """ + availableActions + """
                    -----------------
                    """);
            System.out.println("Your choice: ");
            String action = scanner.nextLine();

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {

                for (int i = 0; i < 30; i++) {
                    System.out.println();
                }
                System.out.println("""
                        --------------------------
                        * GAME PHASE HAS CHANGED *\s
                        moving to Action phase...
                        --------------------------
                        """);
                break;
            }

            processAction(action);

        } while(clientController.getGamePhase().equals(GamePhase.PLANNING));
    }

    public void actionPhase() {

        availableActions = turnActions;

        System.out.println("Action phase has started!");
        String action;

        do {

            clientController.getModelInfo();

            if(!clientController.getGamePhase().equals(GamePhase.ACTION)) {
                break;
            }

            System.out.print("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    """ + availableActions + """
                    -----------------
                    """);
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
                    System.out.print("""
                            ---------------------
                            Available characters:\s
                            (format: name | cost)
                            ---------------------
                            """);
                    ArrayList<Character> characters = clientController.getCharacters();
                    for (Character character : characters) {
                        if(!character.getHasBeenUsed()) {
                            System.out.println(characters.indexOf(character) + ": "
                                    + character.getImage() + " | "
                                    + character.getCost());
                        }
                    }
                    System.out.println("Input character number: ");
                    Integer characterNumber = scanner.nextInt();
                    clientController.playCharacter(characterNumber);
                    scanner.nextLine();
                }

                case playAssistantAction -> {
                    System.out.print("""
                            -----------------------------------
                            Available assistants:\s
                            (format: turnPriority | motherMovs)
                            -----------------------------------
                            """);
                    List<Assistant> deck = clientController.getPlayerInfo().getDeck();
                    for (Assistant assistant : deck) {
                        System.out.println(deck.indexOf(assistant) + ": "
                                + assistant.getTurnPriority() + " | "
                                + assistant.getMotherNatureMovements());
                    }
                    System.out.println("Input assistant number: ");
                    Integer assistantNumber = scanner.nextInt();

                    for (int i = 0; i < 30; i++) {
                        System.out.println();
                    }

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
                }

                case moveMotherNatureAction -> {
                    System.out.println("Input movements: ");
                    Integer steps = scanner.nextInt();

                    scanner.nextLine();
                    clientController.moveMotherNature(steps);
                    availableActions.remove(moveStudentAction);
                    //Adds turn-ending action, equivalent to "pass turn"
                    availableActions.add(chooseCloudAction);
                }

                case chooseCloudAction -> {
                    System.out.println("Available clouds:");
                    for (Cloud cloud : clientController.getClouds()){
                        System.out.println("--------------");
                        System.out.println("ID: " + cloud.getPieceID());
                        System.out.println("Students: ");
                        cloud.getStudents().forEach(student -> {
                            System.out.print(StudentAccessiblePiece.colorOfStudent(student));
                            System.out.print(" ");
                        });
                        System.out.println();
                    }
                    System.out.println("-------------");
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
                    System.out.print("""
                            -----------------
                            Select info type:\s
                            1)
                            """);
                }

                case quitAction -> {
                    System.out.println("Quitting...");
                    clientController.closeConnection();
                }
            }
        }
        String playCharacterAction = "PLAY_CHARACTER";
        String playAssistantAction = "PLAY_ASSISTANT";
        String moveStudentAction = "MOVE_STUDENT";
        String moveMotherNatureAction = "MOVE_MOTHERNATURE";
        String infoAction = "INFO";
        String quitAction = "QUIT";
    }

    public void testingPhase(){

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


    //@Override methods that will be defined in Listener Interface
}
