
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

public class InfoViewEvent extends GameViewEvent {
    public InfoViewEvent(RemoteView view) {
        super(view);
    }
    @Override
    public void accept(RemoteView visitor) {
        //visitor.visit(this);
    }
}
