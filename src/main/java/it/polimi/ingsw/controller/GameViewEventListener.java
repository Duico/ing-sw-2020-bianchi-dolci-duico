package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.event.*;

import java.util.EventListener;

public interface GameViewEventListener extends EventListener {
     void move(MoveViewEvent e);
     void build(BuildViewEvent e);
     void place(PlaceViewEvent e);
     void setPlayerCard(CardViewEvent e);
     void undo(UndoViewEvent e);
     void setFirstPlayer(FirstPlayerViewEvent e);
     void endTurn(EndTurnViewEvent e);
     void challengerCards(ChallengerCardViewEvent e);

}

