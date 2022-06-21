package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.GameController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server main runner class, initializes socket
 */
public class GameServer {

    private final ServerSocket serverSocket;
    private final ExecutorService executorServicePool;
    private final GameController gameController;
    ServerController serverController;

    /**
     * @param port port
     * @throws IOException IOException
     */
    public GameServer(Integer port) throws IOException {
        serverSocket = new ServerSocket(port);
        executorServicePool = Executors.newCachedThreadPool();
        gameController = new GameController();
        serverController = new ServerController(gameController);

        System.out.println("Server running on port " + port);
        System.out.println("Server listening on address " + serverSocket.getLocalSocketAddress() + " and port "
                + serverSocket.getLocalPort());
    }

    /**
     * Accepts new clients and creates and handler for each one
     *
     * @throws IOException IOException
     */
    public void run() throws IOException {
        while(true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New connection established with " + clientSocket.getLocalSocketAddress());
            executorServicePool.submit(new ClientHandler(clientSocket, serverController));
        }
    }

    /**
     * Closes socket and client handlers
     *
     * @throws IOException IOException
     */
    public void close() throws IOException {
        serverSocket.close();
        executorServicePool.shutdown();
    }
}