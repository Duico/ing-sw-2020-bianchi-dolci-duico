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

class DomeBuildTest {
    static Board board;
    static Game game;
    static BuildStrategy domeBuild = new DomeBuild();
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
     * check if the worker can build a dome on a Position which level is < 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkCanBuildDomeOnLevel0() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        assertTrue(domeBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker can build a dome on a Position which level is < 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkCanBuildDomeOnLevel1() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition,destPosition, false);
        assertTrue(domeBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker can build a dome on a Position which level is < 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkCanBuildDomeOnLevel2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition,destPosition, false);
        board.build(startPosition,destPosition, false);
        assertTrue(domeBuild.isValidBuild(startPosition, destPosition, true, board ));
    }

    /**
     * check if the worker can build a dome on a Position which level is = 3 (TOP)
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkCanBuildDomeOnLevel3() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position startPosition = worker0.getCurrentPosition();
        Position destPosition = new Position(startPosition.getX()+1, startPosition.getY());
        board.build(startPosition,destPosition, false);
        board.build(startPosition,destPosition, false);
        board.build(startPosition,destPosition, false);
        assertTrue(domeBuild.isValidBuild(startPosition, destPosition, true, board ));
    }



}