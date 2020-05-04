package it.polimi.ingsw.client;

public class LobbyGuiEvent {
    private String nickname;
    private int numPlayers;

    public LobbyGuiEvent(String nickname, int numPlayers){
        this.numPlayers=numPlayers;
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }
    public int getNumPlayers(){return numPlayers;}

}
