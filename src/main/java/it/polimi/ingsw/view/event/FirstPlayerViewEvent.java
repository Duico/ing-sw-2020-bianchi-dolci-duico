
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

/**
 * Represent message sent from client view to server when challenger player chooses first player of the game
 */
public class FirstPlayerViewEvent extends GameViewEvent {
    private Player player;

    public FirstPlayerViewEvent(Player player, Player firstPlayer){
        super(player);
        this.player=firstPlayer;
    }
    public FirstPlayerViewEvent(Player player){
        this(null, player);
    }

    public Player getFirstPlayer() {
        return player;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}

