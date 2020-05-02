package it.polimi.ingsw.client;

import java.util.EventListener;

public interface LobbyEventListener extends EventListener {
    void sendLobbyEvent(LobbyGuiEvent e);
}
