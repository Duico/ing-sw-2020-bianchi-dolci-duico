package it.polimi.ingsw.server;

import it.polimi.ingsw.view.ViewEventListener;

public interface ClientConnection {

    void closeConnection();

    void addObserver(ViewEventListener observer);

    void asyncSend(Object message);
}
