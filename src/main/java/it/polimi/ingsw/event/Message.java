package it.polimi.ingsw.event;

import it.polimi.ingsw.client.MessageVisitor;

import java.io.Serializable;

/**
 * abstract class used to listen to different types of messages
 */
public abstract class Message implements Serializable {
    public abstract void accept(MessageVisitor visitor);
}
