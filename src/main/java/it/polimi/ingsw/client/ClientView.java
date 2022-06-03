package it.polimi.ingsw.client;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public interface ClientView /*will have to implement listeners*/ {

    void waitGameStartPhase();

    void setupPhase();

    void planningPhase();

    void actionPhase();

    //@Override methods that will be defined in Listener Interface
}
