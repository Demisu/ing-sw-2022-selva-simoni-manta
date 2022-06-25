package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

/**
 * Request to move students
 */
public class MoveStudentRequest implements ClientRequest {
    private Integer studentId;
    private Integer sourceId;
    private Integer targetId;
    private String nickname;

    /**
     * @param studentId studentId
     * @param sourceId sourceId
     * @param targetId targetId
     * @param nickname requester
     */
    public MoveStudentRequest(Integer studentId, Integer sourceId, Integer targetId, String nickname) {
        this.studentId = studentId;
        this.sourceId = sourceId;
        this.targetId = targetId;
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
     * @return studentId
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * @return sourceId
     */
    public Integer getSourceId() {
        return sourceId;
    }

    /**
     * @return targetId
     */
    public Integer getTargetId() {
        return targetId;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }
}
