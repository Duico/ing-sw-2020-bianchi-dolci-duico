package it.polimi.ingsw.client.gui.event;

import it.polimi.ingsw.model.Position;

import java.util.EventListener;
import java.util.List;

/**
 * listener class of gui events
 */
public interface GuiEventListener extends EventListener {
    void onMove(Position startPosition, Position destPosition);
    void onBuild(Position workerPosition, Position buildPosition, boolean isDome);
    void onUndo();
    void onEndTurn();
    void onLogin(String username, Integer numPlayers, boolean persistency);
    void onChooseCard(String chosenCard);
    void onChallengeCards(List<String> challengerCards);
    void onFirstPlayer(String firstPlayer);
    void onPlaceWorker(Position position);
}
