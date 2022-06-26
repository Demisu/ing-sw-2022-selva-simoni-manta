package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Response with operation result
 */
public class OperationResultResponse implements ServerResponse {

    private Boolean result;
    private String message;

    /**
     * @param result result
     * @param message message
     */
    public OperationResultResponse(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * @param handler handler for the response
     */
    @Override
    public void handle(ServerResponseHandler handler) {

    }

    /**
     * @return result
     */
    public Boolean getResult() {
        return result;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
