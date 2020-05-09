
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

public abstract class GameViewEvent extends ViewEvent implements Serializable {
    public GameViewEvent(RemoteView view) {
        super(view);
    }

    public abstract void accept(ViewEventVisitor visitor);
}

