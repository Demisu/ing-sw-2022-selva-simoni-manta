package it.polimi.ingsw.client;

import java.util.Scanner;

public class ClientView /*will have to implement listeners*/ {
    /**
     * holds a reference to the Client's Controller
     */
    private final ClientController controller;

    public ClientView(ClientController controller) {
        this.controller = controller;
    }

    public void nicknamePhase() {
        String nickname = null;

        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose your nickname: ");
            nickname = controller.setPlayerNickname(scanner.nextLine());
            scanner.close();
            System.out.println("Response received. Nickname: " + nickname);
        } while (nickname == null);
    }

    public void waitTurnPhase() {

    }

    public void planningPhase() {

    }

    public void actionPhase() {

    }

    //@Override methods that will be defined in Listener Interface
}