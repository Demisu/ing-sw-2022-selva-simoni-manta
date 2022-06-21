package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientExec;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main class of the game, used to start it (client or server)
 */
public class Eriantys {

    /**
     * Asks which type of application is needed: SERVER, CLI or GUI
     *
     * @param args args
     * @throws IOException IOException
     */
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            System.out.print("""
                    Input the desired application:
                    1) Server
                    2) CLI
                    3) GUI
                    """);
            input = scanner.nextInt();
        } while (input < 1 || input > 3);

        switch (input) {
            case 1:
                Server.main(new String[]{});
            case 2:
                ClientExec.main(new String[]{"CLI"});
            case 3:
                ClientExec.main(new String[]{"GUI"});
        }
    }
}
