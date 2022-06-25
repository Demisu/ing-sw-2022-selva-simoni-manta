package it.polimi.ingsw.model;

/**
 * Enum of all available game phases
 */
public enum GamePhase {

    /**
     * No game present
     */
    OFF,
    /**
     * Creating the game
     */
    SETUP,
    /**
     * Planning round phase of the players
     */
    PLANNING,
    /**
     * Action round phase of the players
     */
    ACTION,
    /**
     * The game has finished
     */
    END;
}
