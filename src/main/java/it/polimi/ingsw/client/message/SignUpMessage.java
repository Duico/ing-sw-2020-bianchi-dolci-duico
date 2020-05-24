package it.polimi.ingsw.client.message;

import java.io.Serializable;

public class SignUpMessage implements Serializable {

    private String nickName;
    private Integer numPlayers;

    public SignUpMessage(String nickName, Integer numPlayers) {
        this.nickName=nickName;
        this.numPlayers=numPlayers;
    }

    public SignUpMessage(String nickName){
        this(nickName, null);
    }

    public String getNickName() {
        return nickName;
    }

    public Integer getNumPlayers() {
        return numPlayers;
    }


}
