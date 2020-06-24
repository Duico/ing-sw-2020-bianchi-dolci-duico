package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;

/**
 * View Connection related to a client
 */
public interface ViewConnection {

    void closeConnection();

    void addObserver(ViewEventListener observer);

    void send(Object message);

    void removeDefeatedPlayer();
}
