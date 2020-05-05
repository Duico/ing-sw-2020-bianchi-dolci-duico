package it.polimi.ingsw.view.event;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

import java.io.Serializable;

public class PlaceViewEvent extends WorkerViewEvent implements Serializable {


    public PlaceViewEvent(RemoteView view, Position destinationPosition){
        super(view,destinationPosition);

    }



    @Override
    public void accept(RemoteView visitor) {
        visitor.visit(this);
    }


}

