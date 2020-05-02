package it.polimi.ingsw.view.event;

import it.polimi.ingsw.message.OperationMessage;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.view.RemoteView;

public class PlaceViewEvent extends WorkerViewEvent{


    public PlaceViewEvent(RemoteView view, Position destinationPosition){
        super(view,destinationPosition);

    }


}

