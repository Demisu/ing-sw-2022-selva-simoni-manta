package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to play assistants
 */
public class PlayAssistantRequest implements ClientRequest {

    private Integer assistantNumber;
    private String nickname;

    /**
     * @param nickname requester
     * @param assistantNumber index of assistant
     */
    public PlayAssistantRequest(String nickname, Integer assistantNumber) {
        this.assistantNumber = assistantNumber;
        this.nickname = nickname;
    }

    /**
     * @param handler handler to handle request
     * @return ServerResponse
     */
    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    /**
     * @return assistantNumber
     */
    public Integer getAssistantNumber() {
        return assistantNumber;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }
}
