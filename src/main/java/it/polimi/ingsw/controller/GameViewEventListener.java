package it.polimi.ingsw.controller;

import it.polimi.ingsw.view.event.*;

import java.util.EventListener;

/**
 * listener interface of all kind of events received from view
 */
public interface GameViewEventListener extends EventListener {

     /**
      * Defines behavior in case of a MoveViewEvent. Check the condition of a valid movement calling model functions
      * @param e move event
      */
     void move(MoveViewEvent e);

     /**
      * Defines behavior in case of a BuildViewEvent. Check the condition of a valid building calling model functions
      * @param e build event
      */
     void build(BuildViewEvent e);

     /**
      * Defines behavior in case of a PlaceViewEvent. Check the condition of a valid place of a worker calling model functions
      * @param e place event
      */
     void place(PlaceViewEvent e);

     /**
      * Defines behavior in case of a CardEvent. Check the condition of the setting of a card calling model functions
      * @param e card event
      */
     void setPlayerCard(CardViewEvent e);

     /**
      * Defines behavior in case of a UndoViewEvent. Check the condition of a valid undo for a player calling model functions
      * @param e undo event
      */
     void undo(UndoViewEvent e);

     /**
      * Defines behavior in case of a FirstPlayerViewEvent. Check the condition of a valid setting of the first player calling model functions
      * @param e first player event
      */
     void setFirstPlayer(FirstPlayerViewEvent e);

     /**
      * Defines behavior in case of a EndTurnViewEvent. Check the condition of a valid end turn calling model functions
      * @param e end turn event
      */
     void endTurn(EndTurnViewEvent e);

     /**
      * Defines behavior in case of a ChallengerCardViewEvent. Check the condition of a valid list of cards calling model functions
      * @param e challenger cards event
      */
     void challengerCards(ChallengerCardViewEvent e);

     /**
      * Defines behavior in case of a InfoViewEvent. Control the status of the turn and emit TurnInfoEvent
      * @param e info event
      */
     void requiredTurnInfo(InfoViewEvent e);

}

