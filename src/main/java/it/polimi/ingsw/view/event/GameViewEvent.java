
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public abstract class GameViewEvent extends ViewEvent{
    public GameViewEvent(RemoteView view) {
        super(view);
    }

    public abstract void accept(ViewEventVisitor visitor);
}

