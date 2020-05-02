package it.polimi.ingsw.message;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Operation;


import java.io.Serializable;

public class OperationMessage implements Serializable {
    private final Operation type;
    private final Position startPosition;
    private final Position destPosition;
    private final boolean isDome;

    public OperationMessage(Operation type, Position startPosition, Position destPosition,  boolean isDome){
        this.type=type;
        this.startPosition=startPosition;
        this.destPosition=destPosition;
        this.isDome=isDome;
    }

    public Operation getType() {
        return type;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public Position getDestPosition() {
        return destPosition;
    }

    public boolean isDome() {
        return isDome;
    }
}
