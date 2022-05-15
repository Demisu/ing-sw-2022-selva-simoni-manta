package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class PlayAssistantRequest implements ClientRequest {

    Integer assistantNumber;

    public PlayAssistantRequest(Integer assistantNumber) {
        this.assistantNumber = assistantNumber;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getAssistantNumber() {
        return assistantNumber;
    }
}
