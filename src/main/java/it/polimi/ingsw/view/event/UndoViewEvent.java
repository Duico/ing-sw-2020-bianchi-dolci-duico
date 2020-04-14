package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;

public class UndoViewEvent extends ViewEvent {
    public UndoViewEvent(Player player) {
        super(player);
    }
}
