package it.polimi.ingsw.model;

/**
 * Represent a specific type of game Turn on which players are allowed just to choose Cards
 */
public class ChoseCardsTurn extends Turn {

    public ChoseCardsTurn(Player currentPlayer) {
        super(TurnPhase.CHOSE_CARDS, currentPlayer);
    }

}
