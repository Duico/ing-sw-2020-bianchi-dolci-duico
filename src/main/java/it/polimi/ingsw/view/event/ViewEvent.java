
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.rmi.Remote;

public class ViewEvent implements Serializable {
    protected RemoteView view;
    public ViewEvent(RemoteView view) {
        this.view = view;
    }

    public RemoteView getView(){
        return this.view;
    }
}

