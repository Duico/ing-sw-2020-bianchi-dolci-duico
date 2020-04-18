package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleBuildTest {
    static Board board;
    static Game game;
    static BuildStrategy multipleBuild = new DoubleBuild();
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
        board.setWorker(worker0, new Position(0, 0));
        board.setWorker(worker1, new Position(1, 1));
        board.setWorker(worker2, new Position(0, 1));
//        board.setWorker(worker3, new Position(3, 3));
    }

    /**
     * check if the worker can build twice on the same position (not dome)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkDoubleBuildSamePositionNotDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addBuild(destPosition);
        assertTrue(multipleBuild.isValidBuild(startPosition, destPosition,false, board));
    }

    /**
     * check if the worker can build twice on the same position (dome)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkDoubleBuildSamePositionDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        Position startPosition2 = new Position(startPosition.getX(), startPosition.getY()+1);
        board.build(startPosition2, destPosition,false);
        board.build(startPosition2, destPosition,false);
        board.build(startPosition2, destPosition,false);
        worker0.addBuild(destPosition);
        assertFalse(multipleBuild.isValidBuild(startPosition, destPosition,true, board));
    }

    /**
     * check if the worker can build twice on different positions
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkDoubleBuildDifferentPosition() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        Position destPosition2 = new Position(startPosition.getX(), startPosition.getY()+1);
        worker0.addBuild(destPosition);
        assertFalse(multipleBuild.isValidBuild(startPosition, destPosition2,false, board));
    }

    /**
     * check if the worker is allowed to build when has moved once
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        assertTrue(multipleBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is allowed to build when has moved and built once
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsAllowedToBuild2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        worker0.addBuild(startPosition);
        assertTrue(multipleBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has only moved
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsRequiredToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        assertTrue(multipleBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has moved and built once
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsRequiredToBuild2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        worker0.addBuild(startPosition);
        assertFalse(multipleBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

}