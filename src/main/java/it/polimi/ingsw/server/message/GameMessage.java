package it.polimi.ingsw.server.message;

import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.cli.ModelEventVisitor;

import java.io.Serializable;

public abstract class GameMessage implements Serializable {
    public abstract void accept(GameMessageVisitor visitor);

}
