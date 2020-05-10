
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class EndTurnViewEvent extends GameViewEvent{

    public EndTurnViewEvent(RemoteView view) {
        super(view);
    }
    public EndTurnViewEvent(){
        this(null);
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}

