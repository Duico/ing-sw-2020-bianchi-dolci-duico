package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;

import java.util.EventObject;

public class ViewEvent {
    protected Player player;
    public ViewEvent(Player player) {
        this.player=player;
    }

    public Player getPlayer(){
        return this.player;
    }
}
