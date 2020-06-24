
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent message sent from client view to server when generic turn ends
 */
public class EndTurnViewEvent extends GameViewEvent{

    public EndTurnViewEvent(Player player) {
        super(player);
    }
    public EndTurnViewEvent(){
        this(null);
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}

