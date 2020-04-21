package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultWinTest {
    static Board board;
    static Game game;
    static WinStrategy defaultWin= new DefaultWin();
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

        Position.setSize(5, 5);
        board = new Board();
        worker0 = new Worker();
        worker1 = new Worker();
        worker2 = new Worker();
        worker3 = new Worker();
//        board.setWorker(worker0, new Position(0, 0));
//        board.setWorker(worker1, new Position(1, 1));
//        board.setWorker(worker2, new Position(2, 2));
//        board.setWorker(worker3, new Position(3, 3));
    }

    /**
     * check if moving from position (0,0) having level=2 to (0,1) having level=3 is a winning move
     * @throws PositionOutOfBoundsException
     */
    @Test
    public void checkIsWinningMove() throws PositionOutOfBoundsException{
        Position startPosition = new Position(0,0);
        Position startPosition2 = new Position(1,0);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.setWorker(worker1,startPosition2);
        board.build(startPosition2, startPosition, false);
        board.build(startPosition2, startPosition, false);
        board.setWorker(worker0,startPosition);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertTrue(defaultWin.isWinningMove(startPosition, destPosition, board));
    }

    /**
     * check if moving from position (0,0) having level=2 to (0,1) having level=2 is a winning move
     * @throws PositionOutOfBoundsException
     */
    @Test
    public void checkIsWinningMove2() throws PositionOutOfBoundsException{
        Position startPosition = new Position(0,0);
        Position startPosition2 = new Position(1,0);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.setWorker(worker1,startPosition2);
        board.build(startPosition2, startPosition, false);
        board.build(startPosition2, startPosition, false);
        board.setWorker(worker0,startPosition);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(defaultWin.isWinningMove(startPosition, destPosition, board));
    }
}