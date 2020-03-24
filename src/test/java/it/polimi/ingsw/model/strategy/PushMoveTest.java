
package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.PushMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Class for testing Class PushMove
 */

class PushMoveTest {
    static Board board;
    static Game game;
    static MoveStrategy pushMove = new PushMove();
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
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (3,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (4,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove3() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove4() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY());
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (4,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove5() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove6() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (3,2)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove7() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()-1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,2)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove8() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()-1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }


    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (1,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-2, startPosition.getY());
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (2,1)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()-2);
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (1,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove3() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-2, startPosition.getY());
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }


    /**
     * Control if return true when the destination cell is right based on the level
     * The worker move up of one level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveUpOneLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of one level in a cell with the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpOneLevelBecauseDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, true);
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of two level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpTwoLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of three level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpThreeLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of one level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownOneLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of two level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownTwoLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of three level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownThreeLevel() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the destination cell level
     * The worker move down in a cell with the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveDownBecauseDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, true);
        System.out.println(board.getBoardCell(destPosition).hasDome());
        assertFalse(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the destination cell feature
     * The worker move down in a occupied cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownHoweverOccupiedCell() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = worker1.getCurrentPosition();
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the destination cell feature
     * The worker move up in a occupied cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveUpHoweverOccupiedCell() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = worker1.getCurrentPosition();
        board.build(destPosition, destPosition, false);
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is occupied
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveHoweverOccupiedCell() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = worker1.getCurrentPosition();
        assertTrue(pushMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is equals the startcell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveBecauseDestCellEqualsStartCell() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        assertFalse(pushMove.isValidMove(startPosition, startPosition, board));
    }



    /**
     * Control if return true when the worker can move
     */
    @Test
    void isAllowedToMove() {
        int numMoves = worker1.getNumMoves();
        int numBuilds = worker1.getNumBuilds();
        assertTrue(pushMove.isAllowedToMove(numMoves, numBuilds));
    }


    /**
     * Control if return true when the worker must move
     */
    @Test
    void isRequiredToMove() {
        int numMoves = worker1.getNumMoves();
        assertTrue(pushMove.isRequiredToMove(numMoves));
    }


    /**
     * Control if return false when the worker can't move
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotAllowedToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,0);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        int numBuilds = worker1.getNumBuilds();
        assertFalse(pushMove.isAllowedToMove(numMoves, numBuilds));
    }


    /**
     * Control if return false when the worker has already move the first time
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotRequiredToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,0);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        assertFalse(pushMove.isRequiredToMove(numMoves));

    }



}
