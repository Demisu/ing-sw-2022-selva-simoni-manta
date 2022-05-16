package it.polimi.ingsw.server;

import java.io.IOException;

public class Server {
    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer(4567);

        try {
            gameServer.run();
        } catch (Exception e) {
            System.out.println("Error occurred in Server thread");
            e.printStackTrace();
        } finally {
            gameServer.close();
        }
    }
}
