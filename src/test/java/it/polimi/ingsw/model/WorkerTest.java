package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.PushMove;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {
    static Worker worker0;

    /**
     * Setup before all the test
     */
    @BeforeEach
    public void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");
        Position.setSize(5,5);
        worker0 = new Worker();
        Position position = new Position(0,0);
        worker0.addMove(position);

    }

    @Test
    public void validCurrentPosition() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position position = new Position(0,0);
        assertTrue(worker0.getCurrentPosition().equals(position));

    }

    @Test
    public void validCurrentPosition2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position position = new Position(0,1);
        worker0.addMove(position);
        assertTrue(worker0.getCurrentPosition().equals(position));

    }

    @Test
    public void correctNumMoves() throws PositionOutOfBoundsException {

        assertEquals(0, worker0.getNumMoves());
    }

    @Test
    public void correctNumMoves2() throws PositionOutOfBoundsException {
        Position position2 = new Position(0,1);
        worker0.addMove(position2);
        assertEquals(1, worker0.getNumMoves());
    }

    @Test
    public void correctNumBuilds() throws PositionOutOfBoundsException {
        Position position = new Position(0,0);
        worker0.addBuild(position);
        assertEquals(1, worker0.getNumBuilds());
    }

    @Test
    public void correctNumBuilds2() throws PositionOutOfBoundsException {
        Position position = new Position(0,0);
        Position position2 = new Position(0,1);
        worker0.addBuild(position);
        worker0.addBuild(position2);
        assertEquals(2, worker0.getNumBuilds());
    }

    @Test
    public void correctNumMovesAndBuilds() throws PositionOutOfBoundsException {

        Position position = new Position(0,1);
        Position position2 = new Position(0,1);
        worker0.addMove(position);
        worker0.addBuild(position2);
        assertEquals(1, worker0.getNumBuilds());
        assertEquals(1, worker0.getNumMoves());
    }

    @Test
    public void correctReset() throws PositionOutOfBoundsException, WorkerPositionNotSetException {

        Position position = new Position(0,1);
        Position position2 = new Position(1,1);
        worker0.addMove(position);
        worker0.addBuild(position2);
        assertEquals(1, worker0.getNumBuilds());
        assertEquals(1, worker0.getNumMoves());
        worker0.reset();
        assertTrue(worker0.getCurrentPosition().equals(position));
        assertEquals(0, worker0.getNumBuilds());
        assertEquals(0, worker0.getNumMoves());
    }

    @Test
    public void correctReset2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position position = new Position(0,0);
        worker0.reset();
        assertTrue(worker0.getCurrentPosition().equals(position));
        assertEquals(0, worker0.getNumBuilds());
        assertEquals(0, worker0.getNumMoves());
    }

    @Test
    public void correctOperationMove() {
        assertEquals(Operation.MOVE, worker0.getLastOperation());
    }

    @Test
    public void correctOperationMove2() throws PositionOutOfBoundsException {
        Position position = new Position(1,1);
        worker0.addMove(position);
        assertEquals(Operation.MOVE, worker0.getLastOperation());
    }

    @Test
    public void correctOperationBuild() throws PositionOutOfBoundsException {
        Position position = new Position(0,1);
        Position position2 = new Position(1,1);
        worker0.addMove(position);
        worker0.addBuild(position2);
        assertEquals(Operation.BUILD, worker0.getLastOperation());
    }

    @Test
    public void correctFirstBuild() throws PositionOutOfBoundsException {
        Position position = new Position(0,1);
        Position position2 = new Position(1,1);
        worker0.addBuild(position);
        worker0.addBuild(position2);
        assertEquals(position, worker0.getFirstBuild());
    }

    @Test
    public void correctClone() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        Position position = new Position(0,1);
        Position position2 = new Position(1,1);
        worker0.addMove(position);
        worker0.addBuild(position2);

        Worker workerClone = worker0.clone();
        assertNotSame(worker0, workerClone);
        assertNotSame(worker0.getMoves(), workerClone.getMoves());
        assertNotSame(worker0.getBuilds(), workerClone.getBuilds());
        assertEquals(worker0.getCurrentPosition(), workerClone.getCurrentPosition());
        assertEquals(worker0.getFirstBuild(), workerClone.getFirstBuild());


    }

    @AfterEach
    void tearDown() {
    }
}