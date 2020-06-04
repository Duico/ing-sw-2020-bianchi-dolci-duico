package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.ViewEventVisitor;

import java.io.Serializable;

public class PlaceViewEvent extends WorkerViewEvent implements Serializable {


    public PlaceViewEvent(Player player, Position destinationPosition){
        super(player,destinationPosition);

    }
    public PlaceViewEvent(Position destinationPosition){
        this(null,destinationPosition);

    }



    @Override
    public void accept(ViewEventVisitor visitor) {
        visitor.emitEvent(this);
    }


}

