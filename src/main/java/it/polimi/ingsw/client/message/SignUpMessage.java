package it.polimi.ingsw.client.message;

import java.io.Serializable;

/**
 * represent message sent from client to server when sign up is requested
 * contains nickname, number of players and persistency option if sent by first player connected to lobby
 * or contains only nickame if sent by other players
 */
public class SignUpMessage implements Serializable {

    private String nickName;
    private Integer numPlayers;
    private boolean persistency = false;

    public SignUpMessage(String nickName, Integer numPlayers, boolean persistency){
        this.nickName=nickName;
        this.numPlayers=numPlayers;
        this.persistency = persistency;
    }

    public SignUpMessage(String nickName){
        this(nickName, null, false);
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
