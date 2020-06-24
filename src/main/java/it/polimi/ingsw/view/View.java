package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public abstract class View extends ViewEventVisitor implements BoardListener, Serializable {
    private Player player;

    public View(Player player) {
        this.player= player;
    }

    public Player getPlayer() {
        return player;
    }
}

