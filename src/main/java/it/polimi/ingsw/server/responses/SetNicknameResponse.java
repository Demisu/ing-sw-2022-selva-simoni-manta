package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.server.ServerResponse;
import it.polimi.ingsw.client.ServerResponseHandler;

/**
 * Response with info on how to proceed client setup
 */
public class SetNicknameResponse implements ServerResponse {

    private Boolean needPlayerNumber;
    private Boolean success;
    private String message;

    /**
     * @param needPlayerNumber if player number is needed
     * @param success success
     * @param message message
     */
    public SetNicknameResponse(Boolean needPlayerNumber, Boolean success, String message) {
        this.needPlayerNumber = needPlayerNumber;
        this.message = message;
        this.success = success;
    }

    /**
     * @param handler handler for the response
     */
    @Override
    public void handle(ServerResponseHandler handler) {
        handler.handle(this);
    }

    /**
     * @return needPlayerNumber
     */
    public Boolean getNeedPlayerNumber() {
        return needPlayerNumber;
    }

    /**
     * @return success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
