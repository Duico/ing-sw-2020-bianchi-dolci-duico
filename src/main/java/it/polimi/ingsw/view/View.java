package it.polimi.ingsw.view;

import it.polimi.ingsw.model.ModelEventEmitter;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.BoardListener;

public class View extends ModelEventEmitter implements BoardListener {
    private Player player;
    public Player getPlayer() {
        return player;
    }
}
