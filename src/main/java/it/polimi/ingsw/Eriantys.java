package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientExec;
import it.polimi.ingsw.server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Eriantys {

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
