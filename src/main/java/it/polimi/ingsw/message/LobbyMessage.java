package it.polimi.ingsw.message;

import java.io.Serializable;

public class LobbyMessage implements Serializable {
    private final String nickName;
    private final int numPlayers;

    public LobbyMessage(String nickName, int numPlayers){
        this.numPlayers=numPlayers;
        this.nickName=nickName;
    }

    public String getNickName() {
        return nickName;
    }
    public int getNumPlayers(){
        return numPlayers;
    }
}
