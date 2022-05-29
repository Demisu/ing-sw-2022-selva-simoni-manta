package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.ClientView;
import it.polimi.ingsw.model.Assistant;
import it.polimi.ingsw.model.GamePhase;

import java.util.List;
import java.util.Scanner;

public class CLI implements ClientView {

    Scanner scanner = new Scanner(System.in);
    String nickname;
    Boolean firstRequest = true;

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
        //nickname = controller.setPlayerNickname(scanner.nextLine());

        if(choosePlayersNumber){
            System.out.print("Choose player number: ");
            Integer number = Integer.parseInt(scanner.nextLine());
            status = clientController.setPlayerNumber(number);
        }else{
            System.out.println("No need to choose player number");
            status = false;
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

    public void planningPhase() {

        do {

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {
                break;
            }

            System.out.println("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    PLAY_ASSISTANT
                    INFO
                    QUIT
                    -----------------
                    """);
            System.out.println("Your choice: ");
            String action = scanner.nextLine();

            clientController.getModelInfo();
            if(!clientController.getGamePhase().equals(GamePhase.PLANNING)) {
                System.out.println("\n\nGAME PHASE HAS CHANGED,\nmoving to Action phase...");
                break;
            }

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
                                + assistant.getTurnPriority() + " | "
                                + assistant.getMotherNatureMovements());
                    }
                    System.out.println("\nInput assistant number: ");
                    Integer assistantNumber = scanner.nextInt();

                    for (int i = 0; i < 30; i++) {
                        System.out.println();
                    }

                    clientController.playAssistant(assistantNumber);
                    scanner.nextLine();
                }
                case "INFO" -> {
                    System.out.println("""
                            -----------------
                            Select info type:\s
                            1)
                            """);
                }
                case "QUIT" -> {
                }
                default -> System.out.println("Invalid action.");
            }
        } while(clientController.getGamePhase().equals(GamePhase.PLANNING));
    }

    public void actionPhase() {

        System.out.println("Action phase has started!");
        String action;

        do {

            clientController.getModelInfo();

            if(!clientController.getGamePhase().equals(GamePhase.ACTION)) {
                break;
            }

            System.out.println("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    PLAY_CHARACTER
                    MOVE_MOTHERNATURE
                    MOVE_STUDENT
                    PASS_TURN
                    -----------------
                    """);
            System.out.println("Your choice: ");
            action = scanner.nextLine();

            switch (action) {
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
        } while(clientController.getGamePhase().equals(GamePhase.ACTION));

        Boolean status = clientController.passTurn();
    }

    //@Override methods that will be defined in Listener Interface
}
