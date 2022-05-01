package it.polimi.ingsw.client;

import it.polimi.ingsw.client.requests.*;

/**
 * A method for every possible Request the Client may make to the Server
 */

public interface ClientRequestHandler {

    ClientResponse handle(MoveMotherNatureRequest req);

    ClientResponse handle(MoveStudentRequest req);

    ClientResponse handle(PlayAssistantRequest req);

    ClientResponse handle(PlayCharacterRequest req);

    ClientResponse handle(PlayerRoundEndedRequest req);

}
