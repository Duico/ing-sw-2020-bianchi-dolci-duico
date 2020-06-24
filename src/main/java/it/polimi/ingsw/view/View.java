package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

/**
 * Represent generic view related to a player
 */
public abstract class View extends ViewEventVisitor implements  Serializable {
    private Player player;

    public View(Player player) {
        this.player= player;
    }

    public Player getPlayer() {
        return player;
    }
}

