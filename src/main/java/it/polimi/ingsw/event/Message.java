package it.polimi.ingsw.event;

import it.polimi.ingsw.client.MessageVisitor;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public abstract void accept(MessageVisitor visitor);
}
