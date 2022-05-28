package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

public class MoveStudentRequest implements ClientRequest {
    Integer studentId;
    Integer sourceId;
    Integer targetId;
    String nickname;

    public MoveStudentRequest(Integer studentId, Integer sourceId, Integer targetId, String nickname) {
        this.studentId = studentId;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.nickname = nickname;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getStudentId() {
        return studentId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public String getNickname() {
        return nickname;
    }
}
