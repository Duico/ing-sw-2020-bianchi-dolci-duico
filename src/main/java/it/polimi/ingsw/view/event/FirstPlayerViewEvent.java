package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class FirstPlayerViewEvent extends ViewEvent {
    private Player firstPlayer;
    public FirstPlayerViewEvent(RemoteView view, Player firstPlayer) {
        super(view);
        this.firstPlayer=firstPlayer;
    }

    public Player getFirstPlayer(){
        return this.firstPlayer;
    }
}
