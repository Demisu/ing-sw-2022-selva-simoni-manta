package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface ClientView /*will have to implement listeners*/ {

    public void waitGameStartPhase();

    public void setupPhase();

    public void testingPhase();

    public void planningPhase();

    public void actionPhase();

    //@Override methods that will be defined in Listener Interface
}
