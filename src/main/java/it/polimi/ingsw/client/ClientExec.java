package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.Scanner;

public class ClientExec {
    public static void main(String[] args) throws IOException {

        int port = 4567;

        Client client = new Client("127.0.0.1", port);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Select type:\nGUI or CLI?");
        ClientController controller = new ClientController(client, scanner.nextLine());

        try {
            client.initConnection();
            controller.run(); //batch of methods to call from the controller when starting the game

        } catch(IOException e) {
            System.out.println("failed initiating client connection");
            e.printStackTrace();
        }
    }
}
