package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.server.responses.GetUpdatedBoardResponse;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Method SocketListeners listens for a server answer on the socket, passing it to the client model
 * class.
 *
 * @author Luca Pirovano
 * @see Runnable
 */
public class SocketListener implements Runnable {

    private final Socket socket;
    private final Game modelView;
    private final ClientController clientController;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final ObjectInputStream inputStream;

    /**
     * Constructor SocketListener creates a new SocketListener instance.
     *
     * @param socket of type Socket - socket reference.
     * @param modelView of type Game - modelView reference.
     * @param inputStream of type ObjectInputStream - the inputStream.
     * @param clientController of type ClientController - ActionHandler reference.
     */
    public SocketListener(
            Socket socket,
            Game modelView,
            ObjectInputStream inputStream,
            ClientController clientController) {
        this.modelView = modelView;
        this.socket = socket;
        this.inputStream = inputStream;
        this.clientController = clientController;
    }

    /**
     * Method process processes the serialized answer received from the server, passing it to the
     * answer handler.
     *
     * @param res of type GetUpdatedBoardResponse - the serialized answer.
     */
    public void process(GetUpdatedBoardResponse res) {
        clientController.handle(res);
    }

    /** Method run loops and sends messages. */
    @Override
    public void run() {
        try {
            do {
                GetUpdatedBoardResponse message = (GetUpdatedBoardResponse) inputStream.readObject();
                process(message);
            } while (clientController.getClient().isConnected());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Connection closed by the server. Quitting...");
            /*if (modelView.getGui() != null) {
                modelView
                        .getGui()
                        .propertyChange(
                                new PropertyChangeEvent(
                                        this, "connectionClosed", null, modelView.getServerAnswer().getMessage()));
            } else {*/
                System.exit(0);
            //}
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage(), e);
            }
        }
    }
}
