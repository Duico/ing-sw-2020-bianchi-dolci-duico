
package it.polimi.ingsw.view.event;

import it.polimi.ingsw.message.SetUpMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class FirstPlayerViewEvent extends GameViewEvent {
    private String player;

    public FirstPlayerViewEvent(RemoteView view, String player){
        super(view);
        this.player=player;
    }
    public FirstPlayerViewEvent(String player){
        this(null, player);
    }

    public String getFirstPlayer() {
        return player;
    }

    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }
}

