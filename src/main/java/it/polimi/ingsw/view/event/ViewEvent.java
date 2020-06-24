
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

/**
 * Represent generic event launched from a client view
 */
public abstract class ViewEvent implements Serializable {

    protected Player player;



    public ViewEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public abstract void accept(ViewEventVisitor visitor);
}

