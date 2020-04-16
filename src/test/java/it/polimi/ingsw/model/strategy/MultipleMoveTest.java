package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.MultipleMove;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MultipleMoveTest {
    static Board board;
    static Game game;
    static MoveStrategy multipleMove = new MultipleMove();
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
    void validPositionOfTheMove() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (3,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove2() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (4,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove3() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove4() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY());
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (4,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove5() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove6() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (3,2)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove7() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()-1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,2)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove8() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()-1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }


    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (1,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-2, startPosition.getY());
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (2,1)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove2() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-1, startPosition.getY()-2);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination position is wrong
     * startPosition (3,3), destPosition (1,3)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove3() throws PositionOutOfBoundsException {
        Position startPosition = worker2.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()-2, startPosition.getY());
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }


    /**
     * Control if return true when the destination cell is right based on the level
     * The worker move up of one level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveUpOneLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of one level in a cell with the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpOneLevelBecauseDome() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, true);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of two level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpTwoLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the level
     * The worker move up of three level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpThreeLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of one level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownOneLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of two level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownTwoLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * The worker move down of three level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validMoveDownThreeLevel() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        assertTrue(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the destination cell level
     * The worker move down in a cell with the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveDownBecauseDome() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition, destPosition, true);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the destination cell feature
     * The worker move down in a occupied cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveDownBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        board.build(startPosition, startPosition, false);
        Position destPosition = worker1.getCurrentPosition();
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is wrong based on the destination cell feature
     * The worker move up in a occupied cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveUpBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = worker1.getCurrentPosition();
        board.build(destPosition, destPosition, false);
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is occupied
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = worker1.getCurrentPosition();
        assertFalse(multipleMove.isValidMove(startPosition, destPosition, board));
    }

    /**
     * Control if return false when the destination cell is equals the startcell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidMoveBecauseDestCellEqualsStartCell() throws PositionOutOfBoundsException {
        Position startPosition = worker0.getCurrentPosition();
        assertFalse(multipleMove.isValidMove(startPosition, startPosition, board));
    }

    /**
     * Control if return true when the position of destination is right
     * startPosition (3,3), destPosition (2,4)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMoveHoweverAlreadyMove() throws PositionOutOfBoundsException {
        Position destinationPosition1= new Position(4,4);
        board.putWorkers(worker2.getCurrentPosition(), destinationPosition1, null);
        Position startPosition = worker2.getCurrentPosition();
        Position destinationPosition2 = new Position(4,3);
        assertTrue(multipleMove.isValidMove(startPosition, destinationPosition2, board));
    }

    /**
     * Control if return false when the position of destination is wrong
     * The worker return in the initial position
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMoveHoweverAlreadyMove() throws PositionOutOfBoundsException {
        Position oldStartPosition = worker2.getCurrentPosition();
        Position destinationPosition1= new Position(4,4);
        board.putWorkers(worker2.getCurrentPosition(), destinationPosition1, null);
        Position startPosition = worker2.getCurrentPosition();
        assertFalse(multipleMove.isValidMove(startPosition, oldStartPosition, board));
    }




    /**
     * Control if return true when the worker can move
     * The worker not ever move
     */
    @Test
    void isAllowedToMove() {

        int numMoves = worker2.getNumMoves();
        int numBuilds = worker2.getNumBuilds();
        assertTrue(multipleMove.isAllowedToMove(numMoves, numBuilds));
    }

    /**
     * Control if return true when the worker can move
     * The worker has already move one time
     */
    @Test
    void isAllowedToMove2() throws PositionOutOfBoundsException {
        Position destinationPosition1= new Position(4,4);
        board.putWorkers(worker2.getCurrentPosition(), destinationPosition1, null);
        int numMoves = worker2.getNumMoves();
        int numBuilds = worker2.getNumBuilds();
        assertTrue(multipleMove.isAllowedToMove(numMoves, numBuilds));
    }


    /**
     * Control if return true when the worker must move
     */
    @Test
    void isRequiredToMove() {
        int numMoves = worker2.getNumMoves();
        assertTrue(multipleMove.isRequiredToMove(numMoves));
    }


    /**
     * Control if return false when the worker can't move
     * The worker has already move 2 times
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotAllowedToMove() throws PositionOutOfBoundsException {
        Position newPosition1 = new Position(4,4);
        worker2.addMove(newPosition1);
        Position newPosition2 = new Position(4,3);
        worker2.addMove(newPosition2);
        int numMoves = worker2.getNumMoves();
        int numBuilds = worker2.getNumBuilds();
        assertFalse(multipleMove.isAllowedToMove(numMoves, numBuilds));
    }


    /**
     * Control if return false when the worker has already move the first time
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotRequiredToMove() throws PositionOutOfBoundsException {
        Position destinationPosition1= new Position(4,4);
        board.putWorkers(worker2.getCurrentPosition(), destinationPosition1, null);
        int numMoves = worker2.getNumMoves();
        assertFalse(multipleMove.isRequiredToMove(numMoves));

    }


}
