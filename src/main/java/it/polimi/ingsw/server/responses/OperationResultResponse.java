package it.polimi.ingsw.server.responses;

import it.polimi.ingsw.client.ServerResponseHandler;
import it.polimi.ingsw.server.ServerResponse;

public class OperationResultResponse implements ServerResponse {

    Boolean result;
    String message;

    public OperationResultResponse(Boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public void handle(ServerResponseHandler handler) {

    }

    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
