package it.polimi.ingsw.client;

public class SetCardGuiEvent {
    private String card;
    public SetCardGuiEvent(String card){
        this.card=card;
    }

    public String getCard() {
        return card;
    }
}
