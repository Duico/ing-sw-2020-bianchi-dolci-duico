
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.ViewEventVisitor;

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
