
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.message.SetUpMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

public class FirstPlayerViewEvent extends GameViewEvent {
    private Player player;

    public FirstPlayerViewEvent(RemoteView view, Player player){
        super(view);
        this.player=player;
    }

    public Player getFirstPlayer() {
        return player;
    }
}

