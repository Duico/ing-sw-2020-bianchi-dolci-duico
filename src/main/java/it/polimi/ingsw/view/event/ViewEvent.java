
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

public abstract class ViewEvent implements Serializable {
    //protected RemoteView view;
    protected Player player;

    /*public ViewEvent(RemoteView view) {
        this.view = view;
    }*/

    /*public RemoteView getView(){
        return this.view;
    }

    public void setView(RemoteView view) {
        this.view = view;
    }
    */

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

