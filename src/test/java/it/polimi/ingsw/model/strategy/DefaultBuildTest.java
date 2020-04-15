package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBuildTest {
    static Board board;
    static Game game;
    static BuildStrategy defaultBuild = new DefaultBuild();
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
    void checkValidBuildWrongDestination() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+2, startPosition.getY());
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on the same position he is on already
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildSamePosition() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX(), startPosition.getY());
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on a position occupied by a different worker
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildOccupiedPosition() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY()+1);
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build on a Position where a dome has already been built on
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildOnDome() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, true);
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build (not a dome) on a position having level <3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel0NotDome() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertTrue(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build a dome on a position having level <3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel0isDome() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker can build (not a dome) on a position having level = 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel3NotDome() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertFalse(defaultBuild.isValidBuild(startPosition, destPosition, false, board ));
    }

    /**
     * check if the worker can build a dome on a position having level = 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkValidBuildLevel3isDome() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        board.build(startPosition, destPosition, false);
        assertTrue(defaultBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker is allowed to build when has never moved or built
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsNotAllowedToBuild1() throws PositionOutOfBoundsException{
        assertFalse(defaultBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is allowed to build when has already both moved and built
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsNotAllowedToBuild2() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        worker0.addBuild(startPosition);
        assertFalse(defaultBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is allowed to build when has only moved once
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        assertTrue(defaultBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is allowed to build when has already moved twice
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsAllowedToBuild2() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        worker0.addMove(startPosition);
        assertTrue(defaultBuild.isAllowedToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has already both moved and built
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsNotRequiredToBuild() throws PositionOutOfBoundsException{
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        worker0.addMove(destPosition);
        worker0.addBuild(startPosition);
        assertFalse(defaultBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }

    /**
     * check if the worker is required to build when has never moved or built
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsRequiredToBuild() throws PositionOutOfBoundsException{
        assertTrue(defaultBuild.isRequiredToBuild(worker0.getNumMoves(), worker0.getNumBuilds(), worker0.getLastOperation()));
    }


}