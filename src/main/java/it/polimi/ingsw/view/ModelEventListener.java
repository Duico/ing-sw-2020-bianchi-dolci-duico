package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.event.*;

import java.util.EventListener;

public interface ModelEventListener extends EventListener {
    void sendMessage(ModelEvent evt);
    void removeDefeatedPlayer(Player player);
//    void movement(MoveWorkerModelEvent e);
//    void build(BuildWorkerModelEvent e);
//    void place(PlaceWorkerModelEvent e);
//    void newTurn(NewTurnModelEvent e);
//    void chosenCards(ChosenCardsModelEvent e);
//    void setCard(SetCardModelEvent e);
//    void defeatPlayer(PlayerDefeatModelEvent e);
//    void fullInfo(FullInfoModelEvent e);
//    void newCardsTurn(NewChoseCardTurnModelEvent e);
//    void winGame(WinModelEvent e);
//    void undo(UndoModelEvent e);

}
