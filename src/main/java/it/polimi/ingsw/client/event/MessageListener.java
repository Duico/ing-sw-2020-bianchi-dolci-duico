package it.polimi.ingsw.client.event;

import it.polimi.ingsw.event.Message;

import java.util.EventListener;

/**
 * listener interface for messages of class Message
 */
public interface MessageListener extends EventListener {
    void handleEvent(Message evt);

}
