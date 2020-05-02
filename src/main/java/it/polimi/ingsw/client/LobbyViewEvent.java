package it.polimi.ingsw.client;

import java.io.Serializable;

public class LobbyViewEvent implements Serializable {
    private String nickname;
    private int numPlayers;

    public LobbyViewEvent(String nickname, int numPlayers){

        this.nickname=nickname;
        this.numPlayers = numPlayers;
    }

    public String getNickname(){
        return nickname;
    }
    public int getNumPlayers(){
        return numPlayers;
    }
}
