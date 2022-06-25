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
import java.util.Date;
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

    //Timeout handling
    private ScheduledExecutorService timeOutTimer;
    private long lastPing;
    private boolean alreadyKO = false;
    private boolean startedTimer = false;
    /**
     * Milliseconds for timeout
     */
    public final long TIME_OUT_TIME = 4000;

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
                if(!startedTimer) {
                    startedTimer = true;
                    timeOutTimer = Executors.newSingleThreadScheduledExecutor();
                    timeOutTimer.scheduleAtFixedRate(() -> {
                        long currentTime = System.currentTimeMillis();
                        if ((lastPing < currentTime - TIME_OUT_TIME)
                                && (controller.getGameController().getCurrentGame().getCurrentPhase().equals(GamePhase.PLANNING)
                                || controller.getGameController().getCurrentGame().getCurrentPhase().equals(GamePhase.ACTION))) {
                            System.out.println("--------------------------------");
                            System.out.println("Connection lost with client of " + clientNickname);
                            System.out.println(TIME_OUT_TIME/1000 + " seconds have passed without a ping response");
                            clientKO();
                            timeOutTimer.shutdown();
                        }
                    }, TIME_OUT_TIME, TIME_OUT_TIME, TimeUnit.MILLISECONDS);
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
            clientKO();
        }

        closeConnection();
    }

    /**
     * Method to handle client disconnection, notifies the controller
     */
    public void clientKO(){
        if(!alreadyKO) {
            alreadyKO = true;
            System.out.println("The client was disconnected");
            System.out.println("--------------------------------");
            controller.playerDisconnected(clientNickname);
            controller.setConnectedPlayers(controller.getConnectedPlayers() - 1);
            closeConnection();
        }
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
