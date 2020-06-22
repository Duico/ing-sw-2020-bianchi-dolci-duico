package it.polimi.ingsw.model;

/**
 * defines particular kind of turn
 * chooseCards : players can only choose cards
 * placeWorkers : players can only place their workers on the Board
 * normal : players can move their worker and build
 */
public enum TurnPhase {
    CHOSE_CARDS, PLACE_WORKERS, NORMAL;
}
