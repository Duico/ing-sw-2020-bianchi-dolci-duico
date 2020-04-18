package it.polimi.ingsw.model.strategy;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import it.polimi.ingsw.model.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class that test the Default Opponent strategy class
 */

class DefaultOpponentTest {
    static Board board;
    static OpponentStrategy defaultOpponent = new DefaultOpponent();
    static Worker worker0;
    static Worker worker1;
    static Worker worker2;
    static Worker worker3;


    /**
     * Setup before all the test
     */
    @BeforeEach
    public void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");

        Position.setSize(5,5);
        board = new Board();
        worker0 = new Worker();
        worker1 = new Worker();
        worker2 = new Worker();
        worker3 = new Worker();
        board.setWorker(worker0, new Position(0,0));
        board.setWorker(worker1, new Position(1,1));
        board.setWorker(worker2, new Position(3,3));

    }

    /**
     * Return true when the cell is empty
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isCellEmpty() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(2,2);
        assertTrue(defaultOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * Return false when the cell is occupied
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isCellEmpty2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker1.getCurrentPosition();
        Position destPosition = new Position(1,1);
        assertFalse(defaultOpponent.isValidPush(startPosition, destPosition, false, board));
    }

}
