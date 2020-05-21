package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.GameMessageVisitor;

import java.io.Serializable;

public abstract class GameMessage implements Serializable {
    public abstract void accept(GameMessageVisitor visitor);

}
