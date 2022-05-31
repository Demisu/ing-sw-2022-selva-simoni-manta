package it.polimi.ingsw.client.requests;

import it.polimi.ingsw.client.ClientRequest;
import it.polimi.ingsw.server.ClientRequestHandler;
import it.polimi.ingsw.server.ServerResponse;

import java.util.List;

public class PlayCharacterRequest implements ClientRequest {

    Integer characterNumber;
    String nickname;
    List<Integer> exchangeOriginList;
    List<Integer> exchangeTargetList;
    List<Integer> piecesToMove; //Id of students to move
    List<Integer> targetPieces; //Id of target island/schoolboard/ecc
    Integer targetID;

    public PlayCharacterRequest(Integer characterNumber, String nickname) {
        this.characterNumber = characterNumber;
        this.nickname = nickname;
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

    public List<Integer> getExchangeOriginList() {
        return exchangeOriginList;
    }

    public List<Integer> getExchangeTargetList() {
        return exchangeTargetList;
    }

    public List<Integer> getPiecesToMove() {
        return piecesToMove;
    }

    public List<Integer> getTargetPieces() {
        return targetPieces;
    }

    public Integer getTargetID() {
        return targetID;
    }
}
