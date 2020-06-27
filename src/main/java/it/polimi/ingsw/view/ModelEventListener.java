package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;

import java.util.EventListener;

/**
 * listener interface for model event messages
 */
public interface ModelEventListener extends EventListener {
    /**
     * send a ModelEvent message
     * @param evt the message
     */
    void sendMessage(ModelEvent evt);

    /**
     * remove the connection of the player from the connectionList
     * @param player
     */
    void removeDefeatedPlayer(Player player);





}
