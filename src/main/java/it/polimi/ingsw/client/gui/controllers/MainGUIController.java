package it.polimi.ingsw.client.gui.controllers;

import it.polimi.ingsw.client.gui.GUI;

public class MainGUIController implements GUIController{

    private GUI gui;

    /** @see GUIController#setGui(GUI) */
    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }
}
