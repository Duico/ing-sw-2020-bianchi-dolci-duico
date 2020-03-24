
package it.polimi.ingsw.model.strategy;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.SwapOpponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class SwapOpponentTest {

    static Board board;
    static OpponentStrategy swapOpponent = new SwapOpponent();


    /**
     * Setup before all the test
     */
    @BeforeEach
    public void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");

        Position.setSize(5,5);
        board = new Board();

    }

    /**
     * Return false when the worker to push is of the same player
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isMyWorker() throws PositionOutOfBoundsException {
        Position startPosition = new Position(0,0);
        Position destPosition = new Position(1,1);
        boolean isOwnWorker = true;
        assertFalse(swapOpponent.isValidPush(startPosition, destPosition, isOwnWorker, board));
    }

    /**
     * Return true when the worker to push is of another player
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotMyWorker() throws PositionOutOfBoundsException {
        Position startPosition = new Position(0,0);
        Position destPosition = new Position(1,1);
        boolean isOwnWorker = false;
        assertTrue(swapOpponent.isValidPush(startPosition, destPosition, isOwnWorker, board));
    }

    /**
     * Control if the destination position swap is equals the start Position
     * @throws PositionOutOfBoundsException
     */
    @Test
    void returnDestCell() throws PositionOutOfBoundsException, InvalidPushCell {
        Position startPosition = new Position(0,0);
        Position destPosition = new Position(1,1);
        assertTrue(startPosition.equals(swapOpponent.destinationPosition(startPosition, destPosition)));
    }
}
