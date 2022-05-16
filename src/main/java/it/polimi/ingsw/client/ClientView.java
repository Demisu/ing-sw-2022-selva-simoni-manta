package it.polimi.ingsw.client;

import java.util.Scanner;

public class ClientView /*will have to implement listeners*/ {
    /**
     * holds a reference to the Client's Controller
     */
    private final ClientController clientController;

    public ClientView(ClientController clientController) {
        this.clientController = clientController;
    }

    public void nicknamePhase() {

        String nickname = null;
        Boolean choosePlayersNumber = false;
        Boolean status = false;
        Scanner scanner = new Scanner(System.in);

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
            System.out.println("tu non scegli.");
            status = false;
        }
        if(status){
            System.out.println("Game Created");
        } else {
            System.out.println("""
                    Game already playing, added to lobby.
                    The game will start as soon as all players have joined.
                    Waiting for all players...""");
        }
        //System.out.println("Response received. Nickname: " + nickname);

        scanner.close();
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
