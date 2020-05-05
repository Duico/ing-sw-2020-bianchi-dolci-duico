
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

public abstract class ViewEvent implements Serializable {
    protected RemoteView view;
    public ViewEvent(RemoteView view) {
        this.view = view;
    }

    public RemoteView getView(){
        return this.view;
    }

    public void setView(RemoteView view) {
        this.view = view;
    }

    public abstract void accept(ViewEventVisitor visitor);
}

