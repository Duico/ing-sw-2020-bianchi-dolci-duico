package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class UndoViewEvent extends ViewEvent {
    public UndoViewEvent(RemoteView view, Player player) {
        super(view);
    }
}
