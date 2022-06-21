package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

import java.util.List;

/**
 * Request to play characters
 */
public class PlayCharacterRequest implements ClientRequest {

    private final Integer characterNumber;
    private final String nickname;
    private Color targetColor;
    private List<Integer> studentsInOrigin; //Id of students to move
    private List<Integer> studentsInTarget; //Id of students to move
    private List<Integer> originPieces; //Id of origin island/schoolboard/ecc
    private List<Integer> targetPieces; //Id of target island/schoolboard/ecc

    /**
     * @param characterNumber number of character
     * @param nickname requester
     * @param targetColor target color
     * @param studentsInOrigin list of students in origin pieces
     * @param studentsInTarget list of students in target pieces
     * @param originPieces origin pieces
     * @param targetPieces target pieces
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, Color targetColor, List<Integer> studentsInOrigin,
                                List<Integer> studentsInTarget, List<Integer> originPieces, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.targetColor = targetColor;
        this.studentsInOrigin = studentsInOrigin;
        this.studentsInTarget = studentsInTarget;
        this.originPieces = originPieces;
        this.targetPieces = targetPieces;
    }

    /**
     * Used for testing character 2, 4, 5, 6, 8
     *
     * @param characterNumber number of character
     * @param nickname requester
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
    }

    /**
     * Used for testing character 1, 11
     *
     * @param characterNumber number of character
     * @param nickname requester
     * @param studentsInOrigin list of students in origin pieces
     * @param targetPieces target pieces
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.targetPieces = targetPieces;
    }

    /**
     * Used for testing character 3, 5
     *
     * @param characterNumber number of character
     * @param nickname requester
     * @param targetPieces target pieces
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.targetPieces = targetPieces;
    }

    /**
     * Used for testing character 7
     *
     * @param characterNumber number of character
     * @param nickname requester
     * @param studentsInOrigin list of students in origin pieces
     * @param originPieces origin pieces
     * @param targetPieces target pieces
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin,
                                List<Integer> originPieces, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.originPieces = originPieces;
        this.targetPieces = targetPieces;
    }

    /**
     * Used for testing character 10
     *
     * @param characterNumber number of character
     * @param nickname requester
     * @param studentsInOrigin list of students in origin pieces
     * @param studentsInTarget list of students in target pieces
     * @param originPieces origin pieces
     * @param targetPieces target pieces
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin, List<Integer> studentsInTarget,
                                List<Integer> originPieces, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.studentsInTarget = studentsInTarget;
        this.originPieces = originPieces;
        this.targetPieces = targetPieces;
    }

    /**
     * Used for testing
     *
     * @param characterNumber number of character
     * @param nickname requester
     * @param targetColor target color
     */
    public PlayCharacterRequest(Integer characterNumber, String nickname, Color targetColor) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.targetColor = targetColor;
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
     * @return characterNumber
     */
    public Integer getCharacterNumber() {
        return characterNumber;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return studentsInOrigin
     */
    public List<Integer> getStudentsInOrigin() {
        return studentsInOrigin;
    }

    /**
     * @return studentsInTarget
     */
    public List<Integer> getStudentsInTarget() {
        return studentsInTarget;
    }

    /**
     * @return targetPieces
     */
    public List<Integer> getTargetPieces() {
        return targetPieces;
    }

    /**
     * @return originPieces
     */
    public List<Integer> getOriginPieces() {
        return originPieces;
    }

    /**
     * @return targetColor
     */
    public Color getTargetColor() {
        return targetColor;
    }
}
