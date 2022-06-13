package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.Scanner;

public class ClientExec {
    public static void main(String[] args) throws IOException {

        int port = 4567;
        String hostAddress;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input IP of server");
        hostAddress = scanner.nextLine();
        if (hostAddress.equals("")){
            hostAddress = "127.0.0.1";
        }
        Client client = new Client(hostAddress, port);

        String ui;
        if(args.length == 0) {
            do {
                System.out.println("Select type:\nCLI (1) or GUI (2)?");
                ui = scanner.nextLine().toUpperCase();
                if (ui.equals("1")) {
                    ui = "CLI";
                } else if (ui.equals("2")) {
                    ui = "GUI";
                }
            } while (!(ui.equals("GUI") || ui.equals("CLI")));
        } else {
            ui = args[0];
        }

        //ClientController controller = new ClientController(client, ui);

        try {
            client.initConnection();
        } catch(IOException e) {
            System.out.println("failed initiating client connection");
            e.printStackTrace();
        }

        ClientController clientController = new ClientController(client, ui);
        clientController.run(); //batch of methods to call from the controller when starting the game
    }
}
