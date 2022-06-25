package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;

/**
 * controller for the whole GUI interface
 */
public interface GUIController {
    /**
     * @param gui gui instance
     */
    void setGui(GUI gui);

    /**
     * called every time a scene is loaded
     */
    void onLoad();
}
