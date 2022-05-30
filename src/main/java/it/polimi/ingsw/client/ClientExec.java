package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.Scanner;

public class ClientExec {
    public static void main(String[] args) throws IOException {

        int port = 4567;
        Client client = new Client("127.0.0.1", port);

        String ui;
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Select type:\nCLI (1) or GUI (2)?");
            ui = scanner.nextLine().toUpperCase();
            if(ui.equals("1")) {
                ui = "CLI";
            } else if (ui.equals("2")) {
                ui = "GUI";
            }
        }while (!(ui.equals("GUI") || ui.equals("CLI")));

        ClientController controller = new ClientController(client, ui);

        try {
            client.initConnection();
            controller.run(); //batch of methods to call from the controller when starting the game

        } catch(IOException e) {
            System.out.println("failed initiating client connection");
            e.printStackTrace();
        }
    }
}
