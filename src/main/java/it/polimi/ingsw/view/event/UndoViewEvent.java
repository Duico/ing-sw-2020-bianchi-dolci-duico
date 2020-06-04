package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

public class UndoViewEvent extends GameViewEvent {


    public UndoViewEvent(Player player){
        super(player);
    }
    public UndoViewEvent(){
        this(null);
    }
    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }


}
