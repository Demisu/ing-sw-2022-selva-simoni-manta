package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class MoveStudentRequest implements ClientRequest {
    Integer studentId;
    Integer sourceId;
    Integer targetId;

    public MoveStudentRequest(Integer studentId, Integer sourceId, Integer targetId) {
        this.studentId = studentId;
        this.sourceId = sourceId;
        this.targetId = targetId;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }
}
