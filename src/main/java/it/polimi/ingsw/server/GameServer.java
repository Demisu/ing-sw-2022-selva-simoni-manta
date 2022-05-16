package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {

    private final ServerSocket serverSocket;
    private final ExecutorService executorServicePool;
    private final GameController gameController;
    ServerController serverController;

    public GameServer(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        executorServicePool = Executors.newCachedThreadPool();
        gameController = new GameController();
        serverController = new ServerController(gameController);

        System.out.println("Server running on port " + port);
        System.out.println("Server listening on address " + serverSocket.getLocalSocketAddress() + " and port "
                + serverSocket.getLocalPort());
    }

    public void run() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection established with " + clientSocket.getLocalSocketAddress());
            executorServicePool.submit(new ClientHandler(clientSocket, serverController));
        }
    }

    public void close() throws IOException {
        serverSocket.close();
        executorServicePool.shutdown();
    }
}