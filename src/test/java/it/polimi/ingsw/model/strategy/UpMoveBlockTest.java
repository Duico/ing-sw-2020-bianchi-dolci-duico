package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpMoveBlockTest {

    static Board board;
    static BlockStrategy upMoveBlock = new UpMoveBlock();
    static MoveStrategy defaultMove = new DefaultMove();
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
        board.setWorker(worker3, new Position(1,0));

    }

    /**
     * Return true when the worker move up
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isBlockMove() throws PositionOutOfBoundsException {
        Position buildPosition = new Position(1,0);
        board.build(worker0.getCurrentPosition(), buildPosition, false);
        assertTrue(upMoveBlock.isBlockMove(worker1.getCurrentPosition(), buildPosition, board));
    }

    /**
     * Return false when the worker doesn't move up
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotBlockMove() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(1,0);
        assertFalse(upMoveBlock.isBlockMove(startPosition, destPosition, board));
    }

    /**
     * Return true when the worker move up
     * @throws PositionOutOfBoundsException
     */
    @Test
    void blockNextPlayerMove() throws PositionOutOfBoundsException {
        Position buildPosition = new Position(1,0);
        board.build(worker0.getCurrentPosition(), buildPosition, false);
        assertTrue(upMoveBlock.blockNextPlayer(worker1.getCurrentPosition(), buildPosition, board));
    }

    /**
     * Return false when the worker doesn't move up
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotBlockNextPlayerMove() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(1,0);
        assertFalse(upMoveBlock.blockNextPlayer(startPosition, destPosition, board));
    }

    @AfterEach
    void tearDown() {
    }
}