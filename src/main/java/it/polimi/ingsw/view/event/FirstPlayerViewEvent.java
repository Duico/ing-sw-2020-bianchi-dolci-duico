
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class FirstPlayerViewEvent extends GameViewEvent {
    private Player player;

    public FirstPlayerViewEvent(RemoteView view, Player player){
        super(view);
        this.player=player;
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

