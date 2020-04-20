package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.View;

import java.io.Serializable;

public class ViewEvent implements Serializable {
    protected View view;
    public ViewEvent(View view) {
        this.view = view;
    }

    public View getView(){
        return this.view;
    }
}
