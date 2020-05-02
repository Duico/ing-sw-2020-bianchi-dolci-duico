package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;

public class MovementGuiEvent {

    private Position StartPosition;
    private Position DestinationPosition;

    public MovementGuiEvent(Position StartPosition, Position DestinationPosition){
        this.StartPosition=StartPosition;
        this.DestinationPosition=DestinationPosition;
    }

    public Position getStartPosition() {
        return StartPosition;
    }

    public Position getDestinationPosition() {
        return DestinationPosition;
    }
}
