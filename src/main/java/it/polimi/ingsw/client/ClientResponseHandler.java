package it.polimi.ingsw.client;

import it.polimi.ingsw.client.responses.*;

/**
 * every method corresponds to 1, and only 1, possible Response to the Client
 */

public interface ClientResponseHandler {

//    -- See RequestSuccessfulResponse --
//    void handle(RequestSuccessfulResponse res);

    void handle(PlayerRoundBeginResponse res);

    void handle(GameBeginNotifyResponse res);

    void handle(GameEndNotifyResponse res);

//    void handle(GameOnPause res); eventually to be implemented to project specification
}
