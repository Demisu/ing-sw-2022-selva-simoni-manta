package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ServerController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable /*Listeners to the View*/{

    private Socket socket;

    private Boolean isClientRunning;

    private final ObjectInputStream in;

    private final ObjectOutputStream out;

    private final ServerController controller;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.controller = new ServerController(this);
    }

    @Override
    public void run() {
        try {
            do {
                ClientResponse clientResponse = ((ClientRequest) in.readObject()).handle((ClientResponseHandler) controller);
                if (clientResponse != null) {
                    try {
                        out.writeObject(clientResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } while (isClientRunning != false);
        } catch (Exception e) {
            System.out.println("Error occurred in ClientHandler thread");
            e.printStackTrace();
        }

        closeConnection();
    }

    private void closeConnection() {
        isClientRunning = false;

        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                System.out.println("Error while closing the input stream");
                e.printStackTrace();
            }
        }

        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                System.out.println("Error while closing the output stream");
                e.printStackTrace();
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing the socket connection");
            e.printStackTrace();
        }
    }

    public void stopClient() {
        this.isClientRunning = false;
    }


    //to implement Listener methods observing the view with @Override
}
