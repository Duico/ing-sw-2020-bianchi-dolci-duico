package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

public class ModelEvent {
    protected Player player;

    public ModelEvent(Player player){
        if(player == null){
            throw new IllegalArgumentException("player not specified");
        }
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    @Override
    public String toString() {
        return "ModelEvent{" +
                "player=" + player.getNickName() +
                '}';
    }
}
