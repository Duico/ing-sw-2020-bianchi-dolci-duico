package it.polimi.ingsw.client.message;

import java.util.EventListener;

public interface SignUpListener extends EventListener {
    void handleEvent(SignUpMessage evt);
}
