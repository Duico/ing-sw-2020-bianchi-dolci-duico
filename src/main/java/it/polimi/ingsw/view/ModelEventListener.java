package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;

import java.util.EventListener;

public interface ModelEventListener extends EventListener {
    void sendMessage(ModelEvent evt);
    void removeDefeatedPlayer(Player player);


}
