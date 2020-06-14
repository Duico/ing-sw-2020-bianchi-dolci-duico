package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    @Test
    void isAllowedToMove() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.isAllowedToMove());
        assertFalse(turn.isAllowedToMove(new Position(0,0)));
    }

    @Test
    void isRequiredToMove() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.isRequiredToMove());
        assertFalse(turn.isRequiredToMove(new Position(0,0)));

    }

    @Test
    void isRequiredToBuild() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.isRequiredToBuild());
        assertFalse(turn.isRequiredToBuild(new Position(0,0)));
    }

    @Test
    void isAllowedToBuild() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.isAllowedToBuild());
        assertFalse(turn.isAllowedToBuild(new Position(0,0)));

    }

    @Test
    void canBuild() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.canBuild(new Board(), new Position(0,0)));
        assertFalse(turn.isFeasibleBuild(new Board(), new Position(0,0), new Position(1,1), false));
        turn.boardBuild(new Board(), new Position(0,0), new Position(1,1), false);
        assertFalse(turn.isSetCurrentWorker());
        assertFalse(turn.isLoseCondition(new Board()));
        assertFalse(turn.checkCurrentWorker(new Position(0,0)));

    }

    @Test
    void canMove() throws PositionOutOfBoundsException {
        Turn turn = new PlaceWorkersTurn(new Player("player1"));
        assertFalse(turn.canMove(new Board(), new Position(0,0)));
        assertFalse(turn.isFeasibleMove(new Board(), new Position(0,0), new Position(1,1)));
        assertFalse(turn.isBlockedMove(new Board(), new Position(0,0), new Position(1,1)));
        assertFalse(turn.getBlockNextPlayer());
        assertNull(turn.boardMove(new Board(), new Position(0,0), new Position(1,1)));
        assertNull(turn.getPreviousTurnCard());
        assertFalse(turn.isWinningMove(new Board(), new Position(0,0), new Position(1,1)));

    }

}