package it.polimi.ingsw.client;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class to execute client and handle its setup/connection
 */
public class ClientExec {

    /**
     * @param args args
     * @throws IOException IOException
     */
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

        try {
            client.initConnection();
        } catch(IOException e) {
            System.out.println("failed initiating client connection");
            client.closeConnection();
            System.exit(-1);
        }

        ClientController clientController = new ClientController(client, ui);
        clientController.run();
    }
}
