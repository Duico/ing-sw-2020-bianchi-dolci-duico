
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

public abstract class GameViewEvent extends ViewEvent implements Serializable {
    public GameViewEvent(Player player) {
        super(player);
    }

    public abstract void accept(ViewEventVisitor visitor);
}

