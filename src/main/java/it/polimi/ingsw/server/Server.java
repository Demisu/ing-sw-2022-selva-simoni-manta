package it.polimi.ingsw.server;
import it.polimi.ingsw.controller.GameController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        GameController controller = new GameController();
        System.out.println("Controller inizializzato");

        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind();
    }
}
