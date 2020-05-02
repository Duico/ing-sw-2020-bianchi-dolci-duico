package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Position;

public class BuildGuiEvent {
    private Position StartPosition;
    private Position DestinationPosition;
    private boolean isDome;

    public BuildGuiEvent(Position StartPosition, Position DestinationPosition, boolean isDome){
        this.StartPosition=StartPosition;
        this.DestinationPosition=DestinationPosition;
        this.isDome=isDome;
    }

    public Position getStartPosition() {
        return StartPosition;
    }

    public Position getDestinationPosition() {
        return DestinationPosition;
    }

    public boolean isDome() {
        return isDome;
    }
}
