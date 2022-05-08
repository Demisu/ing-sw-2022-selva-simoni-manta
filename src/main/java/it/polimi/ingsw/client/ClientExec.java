package it.polimi.ingsw.client;

import java.io.IOException;

public class ClientExec {
    public static void main(String[] args) throws IOException {

        int port = 4567;

        Client client = new Client("127.0.0.1", port);
        ClientController controller = new ClientController(client);

        try {
            client.initConnection();
            controller.run(); //batch of methods to call from the controller when starting the game

        } catch(IOException e) {
            System.out.println("failed initiating client connection");
            e.printStackTrace();
        }
    }
}
