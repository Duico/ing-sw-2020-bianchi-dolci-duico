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

class BuildAtFirstBuildTest {
    static Board board;
    static Game game;
    static BuildStrategy buildAtFirstBuild = new BuildAtFirstBuild();
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
//        board.setWorker(worker2, new Position(2, 2));
//        board.setWorker(worker3, new Position(3, 3));
    }

    /**
     * check if the worker can build in a wrong Position (distance between start Position and destination Position > 1)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildWrongDestination() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+2, startPosition.getY());
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on the same position he is on already
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildSamePosition() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY());
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on a position occupied by a different worker
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildOccupiedPosition() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY()+1);
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on a Position where a dome has already been built on
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildOnDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, true);
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build (not a dome) on a position having level <3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel0NotDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertTrue(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build a dome on a position having level <3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel0isDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker can build (not a dome) on a position having level = 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel3NotDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(buildAtFirstBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build a dome on a position having level = 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel3isDome() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertTrue(buildAtFirstBuild.isValidBuild(startPosition, destPosition, true, board ));
    }
    /**
     * check if the worker can build when has never moved or built
     */
    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException{
        assertTrue(buildAtFirstBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker can build when has already moved once
     */
    @Test
    void checkIsAllowedToBuild2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        assertTrue(buildAtFirstBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker can build when has already built and moved
     */
    @Test
    void checkIsAllowedToBuild3() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addBuild(destPosition);
        worker0.addMove(startPosition);
        assertTrue(buildAtFirstBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker can build when has already moved and built
     */
    @Test
    void checkIsNotAllowedToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(startPosition);
        worker0.addBuild(destPosition);
        assertFalse(buildAtFirstBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has never moved or built
     */
    @Test
    void checkIsRequiredToBuild() throws PositionOutOfBoundsException{
        assertTrue(buildAtFirstBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has already built and moved
     */
    @Test
    void checkIsRequiredToBuild2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addBuild(destPosition);
        worker0.addMove(startPosition);
        assertTrue(buildAtFirstBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has already built and moved
     */
    @Test
    void checkIsNotRequiredToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(startPosition);
        worker0.addBuild(destPosition);
        assertFalse(buildAtFirstBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }



}