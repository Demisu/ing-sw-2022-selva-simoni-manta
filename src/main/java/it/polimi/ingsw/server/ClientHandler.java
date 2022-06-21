package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.requests.SetNicknameRequest;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handler for client communication
 */
public class ClientHandler implements Runnable {

    private Socket socket;
    private Boolean isClientStopped = false;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final ServerController controller;
    private String clientNickname;

    /**
     * @param socket socket reference
     * @param mainServerController mainServerController
     * @throws IOException IOException
     */
    public ClientHandler(Socket socket, ServerController mainServerController) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.controller = mainServerController;
    }

    /**
     * Run method to be used during execution, handles in and out stream
     */
    @Override
    public void run() {
        try {
            do {
                ClientRequest request = ((ClientRequest) in.readObject());
                ServerResponse serverResponse = request.handle(controller);
                //If the client connects correctly, save his nickname for disconnection
                if(request instanceof SetNicknameRequest && ((SetNicknameResponse) serverResponse).getSuccess()) {
                    clientNickname = ((SetNicknameRequest) request).getNickname();
                }
                 if (serverResponse != null) {
                     try {
                         out.writeObject(serverResponse);
                         out.flush();
                         out.reset();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                }
            } while (isClientStopped == null || !isClientStopped);
        } catch (Exception e) {
            System.out.println("The client was disconnected");
            controller.playerDisconnected(clientNickname);
            e.printStackTrace();
        }

        closeConnection();
    }

    /**
     * Closes client connection
     */
    private void closeConnection() {
        isClientStopped = true;

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

    /**
     * sets isClientStopped to true
     */
    public void stopClient() {
        this.isClientStopped = true;
    }
}
