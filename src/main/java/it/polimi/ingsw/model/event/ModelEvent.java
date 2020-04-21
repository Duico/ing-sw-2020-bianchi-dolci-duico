package it.polimi.ingsw.model.event;

import java.util.UUID;

public class ModelEvent {
    protected UUID playerUUID;

    public ModelEvent(UUID playerUUID){
        if(this.playerUUID == null){
            throw new IllegalArgumentException("player not specified");
        }
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID(){
        return this.playerUUID;
    }

    @Override
    public String toString() {
        return "ModelEvent{" +
                "player=" + playerUUID +
                '}';
    }
}
