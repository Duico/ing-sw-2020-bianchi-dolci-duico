package it.polimi.ingsw.message;

import java.io.Serializable;

public class LobbyMessage implements Serializable {
    private final String nickName;

    public LobbyMessage(String nickName){
        this.nickName=nickName;
    }

    public String getNickName() {
        return nickName;
    }
}
