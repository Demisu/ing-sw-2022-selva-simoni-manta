package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private String hostAddress;

    private final int port;

    private Socket connection;

    private ObjectInputStream in;

    private ObjectOutputStream out;

    public Client(String hostAddress, Integer port) {
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public void initConnection() throws IOException {
        connection = new Socket(hostAddress, port);
        in = new ObjectInputStream(connection.getInputStream());
        out = new ObjectOutputStream(connection.getOutputStream());
    }

    public void closeConnection() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    /**
     * Repeatedly processes the next Response received by the client
     * @return supertype Response type, or null
     */
    public ClientResponse clientResponse() {
        try {
            return (ClientResponse) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error in Client response");
        }

        return null;
    }

    /**
     * Wraps the Request in its supertype to send it out to the Server
     * @param req the Request object to send
     */
    public void clientRequest(ClientRequest req) {
        try {
            out.writeObject(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
