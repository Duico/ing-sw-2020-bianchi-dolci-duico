package it.polimi.ingsw.model.event;

import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.server.message.GameMessage;
import it.polimi.ingsw.model.Player;

/**
 * Represent generic event launched from Model
 */
public abstract class ModelEvent extends GameMessage  {
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

    public void accept(MessageVisitor visitor){
        visitor.visit(this);
    }

    public abstract void accept(ModelEventVisitor visitor);

}
