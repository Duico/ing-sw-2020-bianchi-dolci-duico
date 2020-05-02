
package it.polimi.ingsw.client;

import java.util.EventListener;

public interface MovementEventListener extends EventListener {
    void move(MovementGuiEvent e);
}

