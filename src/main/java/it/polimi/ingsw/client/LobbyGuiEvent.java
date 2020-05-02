package it.polimi.ingsw.client;

public class LobbyGuiEvent {
    private String nickname;

    public LobbyGuiEvent(String nickname){
        this.nickname=nickname;
    }

    public String getNickname(){
        return nickname;
    }

}
