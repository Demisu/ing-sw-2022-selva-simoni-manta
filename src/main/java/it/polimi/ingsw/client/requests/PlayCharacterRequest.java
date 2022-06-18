package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

import java.util.List;

public class PlayCharacterRequest implements ClientRequest {

    private final Integer characterNumber;
    private final String nickname;
    private Color targetColor;
    private List<Integer> studentsInOrigin; //Id of students to move
    private List<Integer> studentsInTarget; //Id of students to move
    private List<Integer> originPieces; //Id of origin island/schoolboard/ecc
    private List<Integer> targetPieces; //Id of target island/schoolboard/ecc

    //General constructor
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

    //Used for character 2, 4, 5, 6, 8
    public PlayCharacterRequest(Integer characterNumber, String nickname) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
    }

    //Used for character 1, 11
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.targetPieces = targetPieces;
    }

    //Used for character 3, 5
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.targetPieces = targetPieces;
    }

    //Used for character 7
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin,
                                List<Integer> originPieces, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.originPieces = originPieces;
        this.targetPieces = targetPieces;
    }

    //Used for character 10
    public PlayCharacterRequest(Integer characterNumber, String nickname, List<Integer> studentsInOrigin, List<Integer> studentsInTarget,
                                List<Integer> originPieces, List<Integer> targetPieces) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.studentsInOrigin = studentsInOrigin;
        this.studentsInTarget = studentsInTarget;
        this.originPieces = originPieces;
        this.targetPieces = targetPieces;
    }

    //Used for character 9, 12
    public PlayCharacterRequest(Integer characterNumber, String nickname, Color targetColor) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
        this.targetColor = targetColor;
    }

    @Override
    public ServerResponse handle(ClientRequestHandler handler) {
        return handler.handle(this);
    }

    public Integer getCharacterNumber() {
        return characterNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public List<Integer> getStudentsInOrigin() {
        return studentsInOrigin;
    }

    public List<Integer> getStudentsInTarget() {
        return studentsInTarget;
    }

    public List<Integer> getTargetPieces() {
        return targetPieces;
    }

    public List<Integer> getOriginPieces() {
        return originPieces;
    }

    public Color getTargetColor() {
        return targetColor;
    }
}
