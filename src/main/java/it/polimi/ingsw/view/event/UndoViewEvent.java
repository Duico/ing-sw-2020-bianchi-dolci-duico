package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent message sent from client view to server when a player tries to undo his last operation
 */
public class UndoViewEvent extends GameViewEvent {


    public UndoViewEvent(Player player){
        super(player);
    }
    public UndoViewEvent(){
        this(null);
    }
    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }


}
