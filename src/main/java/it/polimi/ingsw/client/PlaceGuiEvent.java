package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;

public class PlaceGuiEvent {
    private Position position;

    public PlaceGuiEvent(Position position){
        this.position=position;
    }


    public Position getPosition(){
        return position;
    }
}
