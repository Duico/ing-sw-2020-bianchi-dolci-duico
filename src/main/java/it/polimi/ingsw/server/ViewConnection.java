package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;

/**
 * View Connection related to a client
 */
public interface ViewConnection {

    /**
     * close the socket gracefully
     */
    void closeConnection();


    void addObserver(ViewEventListener observer);

    /**
     * send a message through the output stream
     * @param message
     */
    void send(Object message);

    /**
     * remove the connection from the connectionList
     */
    void removeDefeatedPlayer();
}
