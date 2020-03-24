package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NormalTurnTest {

    static Board board;
    static Card card;
    static NormalTurn turn;
    static Player player1;
    static Worker worker1;
    static Worker worker2;

    @BeforeEach
    void before() throws StrategyNameNotFound, PositionOutOfBoundsException {
        card = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        player1 = new Player("player1");
        turn = new NormalTurn(player1, card, false);
        board = new Board();
        worker1 = new Worker();
        worker2 = new Worker();
        Position pos0 = new Position(0, 0);
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1, 0);
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        player1.setCard(card);
        board.setWorker(worker1, pos0);
        turn.updateCurrentWorker(0);
    }

    @Test
    void checkIsAllowedToMove() throws WorkerPositionNotSetException {
        assertTrue(turn.isAllowedToMove());
    }

    @Test
    void checkIsRequiredToMove() {
        assertTrue(turn.isRequiredToMove());
    }

    @Test
    void checkIsRequiredToMove2() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertFalse(turn.isRequiredToMove());
    }

    @Test
    void checkIsRequiredToBuild() {
        assertTrue(turn.isRequiredToBuild());
    }

    @Test
    void checkIsRequiredToBuild2() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertTrue(turn.isRequiredToBuild());
    }

    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertTrue(turn.isAllowedToBuild());
    }


}