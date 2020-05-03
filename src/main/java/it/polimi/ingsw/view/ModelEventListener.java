package it.polimi.ingsw.view;

import it.polimi.ingsw.model.event.*;

import java.util.EventListener;

public interface ModelEventListener extends EventListener {
    void movement(MoveWorkerModelEvent e);
    void build(BuildWorkerModelEvent e);
    void place(PlaceWorkerModelEvent e);
    void newTurn(NewTurnModelEvent e);
    void chosenCards(ChosenCardsModelEvent e);
    void setCard(SetCardModelEvent e);
    void removePlayer(PlayerRemovalModelEvent e);
    void fullInfo(FullInfoModelEvent e);
    void newCardsTurn(NewChoseCardTurnModelEvent e);
    void winGame(WinModelEvent e);

}
