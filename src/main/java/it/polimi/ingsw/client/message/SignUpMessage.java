package it.polimi.ingsw.client.message;

import java.io.Serializable;

public class SignUpMessage implements Serializable {

    private String nickName;
    private Integer numPlayers;
    private boolean persistency = false;

    public SignUpMessage(String nickName, Integer numPlayers, boolean persistency){
        this(nickName, numPlayers);
        this.persistency = persistency;
    }
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


    public boolean isPersistency() {
        return persistency;
    }
}
