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
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Choose your nickname: ");
            nickname = scanner.nextLine();
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
                System.out.println("Game already playing, added to lobby");
            }
            nickname = scanner.nextLine();

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
