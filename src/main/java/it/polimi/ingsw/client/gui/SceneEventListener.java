package it.polimi.ingsw.client.gui;

import java.util.EventListener;

/**
 * listener class of scene events
 */
public interface SceneEventListener extends EventListener {
    void handleEvent(SceneEvent evt);
}
