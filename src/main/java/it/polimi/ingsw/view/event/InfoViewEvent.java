
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent message sent from client view to server when a player asks informations about the game
 * this particular message is used in GUI to update game buttons after every operation
 */
public class InfoViewEvent extends GameViewEvent {
    public InfoViewEvent(Player player) {
        super(player);
    }
    public InfoViewEvent(){
        this(null);
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}
