
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.view.RemoteView;

public class GameViewEvent extends ViewEvent{
    public GameViewEvent(RemoteView view) {
        super(view);
    }

    @Override
    public void accept(RemoteView visitor) {

    }
}

