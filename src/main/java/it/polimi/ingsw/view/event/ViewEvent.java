package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class ViewEvent {
    protected RemoteView view;
    public ViewEvent(RemoteView view) {
        this.view= view;
    }

    public RemoteView getView(){
        return this.view;
    }
}
