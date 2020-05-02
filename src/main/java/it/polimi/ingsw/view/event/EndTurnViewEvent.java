
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class EndTurnViewEvent extends GameViewEvent{

    public EndTurnViewEvent(RemoteView view) {
        super(view);
    }
}

