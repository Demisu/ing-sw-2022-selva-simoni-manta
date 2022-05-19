package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientView /*will have to implement listeners*/ {

    Scanner scanner = new Scanner(System.in);
    String nickname;

    /**
     * holds a reference to the Client's Controller
     */

    private final ClientController clientController;

    public ClientView(ClientController clientController) {
        this.clientController = clientController;
    }

    public void testingPhase(){

        Scanner scanner = new Scanner(System.in);
        String action;

        do {
            clientController.getModelInfo(nickname);

            System.out.println("""
                    -----------------
                    Choose an action:\s
                    -----------------
                    Allowed:
                    PLAY_ASSISTANT
                    PLAY_CHARACTER
                    MOVE_MOTHERNATURE
                    MOVE_STUDENT
                    QUIT
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
                    clientController.playAssistant(nickname, scanner.nextInt());
                }
                case "PLAY_CHARACTER" -> {
                    System.out.println("\nInput character number: ");
                    clientController.playCharacter(scanner.nextInt());
                }
                case "MOVE_MOTHERNATURE" -> {
                    System.out.println("\nInput movements: ");
                    clientController.moveMotherNature(scanner.nextInt());
                }
                case "MOVE_STUDENT" -> {
                    System.out.println("\nInput student ID: ");
                    Integer student = scanner.nextInt();
                    System.out.println("\nInput source ID: ");
                    Integer source = scanner.nextInt();
                    System.out.println("\nInput target ID: ");
                    Integer target = scanner.nextInt();
                    clientController.moveStudent(student, source, target);
                }
                default -> System.out.println("Invalid action.");
            }
        } while(!action.equals("QUIT"));

        scanner.close();
    }

    public void nicknamePhase() {

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
            status = clientController.setPlayerNumber(nickname, number);
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
                    Waiting for all players...""");
        }
        //System.out.println("Response received. Nickname: " + nickname);
    }

    public void waitTurnPhase() {

        Boolean status = false;
        System.out.println("Waiting further implementations");
        Scanner scanner = new Scanner(System.in);
        String placeholder = scanner.nextLine();

        //TO CHECK
        status = clientController.waitTurn();
        System.out.println("Waiting your turn...");

    }

    public void planningPhase() {

        System.out.println("Planning phase has started!");
    }

    public void actionPhase() {

        System.out.println("Action phase has started!");
    }

    //@Override methods that will be defined in Listener Interface
}
