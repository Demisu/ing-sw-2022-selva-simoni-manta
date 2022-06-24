package it.polimi.ingsw.client;

import it.polimi.ingsw.server.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Client class used for server connection and communication
 */
public class Client {

    private final String hostAddress;
    private final int port;

    private Socket connection;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Boolean isConnected = false;

    /**
     * @param hostAddress IP of the host (server)
     * @param port port of the host (server)
     */
    public Client(String hostAddress, Integer port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    /**
     * Initiates client connection
     *
     * @throws IOException IOException
     */
    public void initConnection() throws IOException {
        connection = new Socket(hostAddress, port);
        in = new ObjectInputStream(connection.getInputStream());
        out = new ObjectOutputStream(connection.getOutputStream());
        isConnected = true;
    }

    /**
     * Closes client connection
     *
     * @throws IOException IOException
     */
    public void closeConnection() throws IOException {
        in.close();
        out.close();
        connection.close();
        isConnected = false;
    }

    /**
     * Repeatedly processes the next Response received by the client
     * @return supertype Response type, or null
     */
    public synchronized ServerResponse clientResponse() {
        try {
            return (ServerResponse) in.readObject();
        } catch (IOException e) {
            System.out.println("Failed to communicate with server, shutting down the application...");
            try {
                closeConnection();
            } catch (IOException ex) {
                System.out.println("Also failed to close connection");
            }
            System.exit(-1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error in Client response");
        }

        return null;
    }

    /**
     * Wraps the Request in its supertype to send it out to the Server
     * @param req the Request object to send
     */
    public synchronized void clientRequest(ClientRequest req) {
        try {
            out.writeObject(req);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return if the client is connected
     */
    public Boolean isConnected() {
        return isConnected;
    }
}
