package it.polimi.ingsw.server;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ViewEventListener;

public interface ViewConnection {

    void closeConnection();

    void addObserver(ViewEventListener observer);

    void asyncSend(Object message);

    void playerDefeat();
}
