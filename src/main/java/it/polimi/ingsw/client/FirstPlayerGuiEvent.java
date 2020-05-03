package it.polimi.ingsw.client;

public class FirstPlayerGuiEvent {
    private String firstPlayer;
    public FirstPlayerGuiEvent(String firstPlayer){
        this.firstPlayer=firstPlayer;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }
}
