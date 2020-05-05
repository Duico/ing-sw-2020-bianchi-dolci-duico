package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.UUID;

public class ModelEvent implements Serializable {
    protected Player player;

    public ModelEvent(Player player){
        if(player == null || player.getUuid() == null){
            throw new IllegalArgumentException("player not specified");
        }
        this.player = player;
    }

//    public UUID getPlayerUUID(){
//        return this.playerUUID;
//    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "ModelEvent{" +
                "player=" + player.getUuid() +
                '}';
    }
}
