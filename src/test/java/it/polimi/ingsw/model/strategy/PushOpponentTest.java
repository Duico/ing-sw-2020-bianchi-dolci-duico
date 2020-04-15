package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PushOpponentTest {

    static Board board;
    static Game game;
    static OpponentStrategy pushOpponent = new PushOpponent();
    static Worker worker0;
    static Worker worker1;
    static Worker worker2;
    static Worker worker3;
    static Worker worker4;
    static Worker worker5;

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
        worker4 = new Worker();
        worker5 = new Worker();
        board.setWorker(worker0, new Position(0,0));
        board.setWorker(worker1, new Position(1,1));
        board.setWorker(worker2, new Position(3,3));
        board.setWorker(worker3, new Position(1,0));
        board.setWorker(worker4, new Position(2,2));
        board.setWorker(worker5, new Position(2,0));


    }

    /**
     * return false when the destination cell contain a worker of the same player
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isOwnWorker() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(0,1);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, true, board));
    }

    /**
     * return false when the destination cell contain a worker of the same player
     * @throws PositionOutOfBoundsException
     */
    @Test
    void destPositionIsFree() throws PositionOutOfBoundsException {
        Position startPosition = worker1.getCurrentPosition();
        Position destPosition = new Position(2,1);
        assertTrue(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * push out of the board in diagonal
     */
    @Test
    void pushOutOfTheBoardDiagonal() throws PositionOutOfBoundsException {
        Position startPosition = worker1.getCurrentPosition();
        Position destPosition = new Position(0,0);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * push out of the board in orizontal
     */
    @Test
    void pushOutOfTheBoardOrizontal() throws PositionOutOfBoundsException {
        Position startPosition = worker3.getCurrentPosition();
        Position destPosition = new Position(0,0);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * push out of the board in vertical
     */
    @Test
    void pushOutOfTheBoardVertical() throws PositionOutOfBoundsException {
        Position startPosition = worker1.getCurrentPosition();
        Position destPosition = new Position(1,0);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * invalid push because the destination cell of the push is occupied
     */
    @Test
    void invalidPushDiagonal() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(1,1);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * invalid push because the destination cell of the push has a dome
     */
    @Test
    void invalidPushDiagonalBecauseDome() throws PositionOutOfBoundsException {
        Position startPosition = worker4.getCurrentPosition();
        Position destPosition = new Position(3,3);
        Position domePosition = new Position(4,4);
        board.build(worker2.getCurrentPosition(), domePosition, true);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * valid push because the destination cell of the push is empty
     */
    @Test
    void validPushDiagonal() throws PositionOutOfBoundsException {
        Position startPosition = worker4.getCurrentPosition();
        Position destPosition = new Position(3,3);
        assertTrue(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * valid push because the destination cell of the push is empty
     */
    @Test
    void validPushVertical() throws PositionOutOfBoundsException {
        Position startPosition = worker3.getCurrentPosition();
        Position destPosition = new Position(1,1);
        assertTrue(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * invalid push because the destination cell of the push is occupied
     */
    @Test
    void invalidPushOrizontal() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(1,0);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * invalid push because the destination cell of the push has a dome
     */
    @Test
    void invalidPushOrizontalBeacuseDome() throws PositionOutOfBoundsException {
        Position startPosition = worker3.getCurrentPosition();
        Position destPosition = new Position(2,0);
        Position domePosition = new Position(3,0);
        board.build(worker5.getCurrentPosition(), domePosition, true);
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }

    /**
     * invalidPushCell
     */
    @Test
    void invalidPushPosition() throws PositionOutOfBoundsException, InvalidPushCell {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = worker2.getCurrentPosition();
        assertFalse(pushOpponent.isValidPush(startPosition, destPosition, false, board));
    }


    @AfterEach
    void tearDown() {
    }
}