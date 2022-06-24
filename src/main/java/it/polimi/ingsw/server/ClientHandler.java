package it.polimi.ingsw.server;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.client.requests.SetNicknameRequest;
import it.polimi.ingsw.model.GamePhase;
import it.polimi.ingsw.server.responses.SetNicknameResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ScheduledExecutorService timeOutTimer;
    private long lastPing;
    //Milliseconds for timeout
    public final long TIME_OUT_TIME = 2000;

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
     * Run method to be used during execution, handles in and out stream.
     * If the client doesn't respond in 2 seconds, marks it as offline.
     */
    @Override
    public void run() {
        try {
            do {
                ClientRequest request = ((ClientRequest) in.readObject());
                lastPing = System.currentTimeMillis();
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
                         timeOutTimer = Executors.newSingleThreadScheduledExecutor();
                         timeOutTimer.schedule(() -> {
                             if((lastPing < System.currentTimeMillis() - TIME_OUT_TIME)
                                     && (controller.getGameController().getCurrentGame().getCurrentPhase().equals(GamePhase.PLANNING)
                                         || controller.getGameController().getCurrentGame().getCurrentPhase().equals(GamePhase.ACTION)) ){
                                 clientKO();
                             }
                         }, 3, TimeUnit.SECONDS);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                }
            } while (isClientStopped == null || !isClientStopped);
        } catch (Exception e) {
            clientKO();
        }

        closeConnection();
    }

    /**
     * Method to handle client disconnection, notifies the controller
     */
    public void clientKO(){
        System.out.println("The client was disconnected");
        controller.playerDisconnected(clientNickname);
        controller.setConnectedPlayers(controller.getConnectedPlayers() - 1);
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
