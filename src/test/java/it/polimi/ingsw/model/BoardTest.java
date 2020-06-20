package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board b;

    @BeforeEach
    void setUp() throws  Exception{
        b = new Board();
        Worker w = new Worker();
        b.setWorker(w, new Position(3,2));
    }
    @Test
    void buildWithNullWorker() throws  Exception{
        Position startPosition = new Position(1,1);
        Position destinationPosition = new Position(2,2);
        assertThrows(Exception.class, ()->{
            b.build(startPosition, destinationPosition, false);
        });
    }
    @Test
    void buildAllButDome() throws Exception {
        Position startPosition = new Position(3,2);
        Position destinationPosition = new Position(2,2);
        assertEquals(Level.EMPTY, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.BASE, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.MID, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.TOP, b.getBoardCell(destinationPosition).getLevel());

    }
    @Test
    void buildAllAndDome() throws Exception {
        Position startPosition = new Position(3,2);
        Position destinationPosition = new Position(2,2);
        assertEquals(Level.EMPTY, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.BASE, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.MID, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, false);
        assertEquals(Level.TOP, b.getBoardCell(destinationPosition).getLevel());

        b.build(startPosition, destinationPosition, true);
        assertEquals(Level.TOP, b.getBoardCell(destinationPosition).getLevel());

        assertTrue(b.getBoardCell(destinationPosition).hasDome());
    }
    @Test
    void getBoardCell() throws Exception{
        Position startPosition = new Position(3,2);
        Position destinationPosition = new Position(2,2);
        b.build(startPosition, destinationPosition, false);
        BoardCell bc = b.getBoardCell(destinationPosition);
        //assertNotEquals(bc, b.getBoardCellReference(destinationPosition));
        bc.setLevel(Level.EMPTY);
        b.build(startPosition, destinationPosition, false);
        assertNotEquals(b.getBoardCell(destinationPosition).getLevel(), bc.getLevel());

    }
    @Test
    void setWorker() throws Exception{
        Position startPosition = new Position(2,2);
        Worker w2 = new Worker();
        assertTrue(b.setWorker(w2, startPosition));
        //TODO
        //no idea why this doesnt work
        assertNotSame(w2, b.getBoardCell(startPosition).getWorker());
    }
    @Test
    void setWorkerFail() throws Exception{
        Position startPosition = new Position(3,2);
        Worker w2 = new Worker();
        assertFalse(b.setWorker(w2, startPosition));
        assertNotEquals(w2, b.getBoardCell(startPosition).getWorker());
    }

    @Test
    void pushMovePutWorkers() throws Exception {
        Position startPosition = new Position(3,2);
        Position destinationPosition = new Position(2,2);
        Position pushDestPosition = new Position(1,2);
        Worker w2 = new Worker();
        b.setWorker(w2, destinationPosition);
        Worker w1 = b.getBoardCell(startPosition).getWorker();
        assertEquals(null, b.getBoardCell(pushDestPosition).getWorker());
        b.putWorkers(startPosition, destinationPosition, pushDestPosition);
        assertNotEquals(null, b.getBoardCell(pushDestPosition).getWorker());
        assertNotEquals(null, b.getBoardCell(destinationPosition).getWorker());
        assertEquals(null, b.getBoardCell(startPosition).getWorker());
    }

    @Test
    void swapMovePutWorkers() throws Exception {
        Position startPosition = new Position(3,3);
        Position destinationPosition = new Position(2,2);
        Position pushDestPosition = new Position(3,3);
        Worker w1 = new Worker();
        b.setWorker(w1, startPosition);
        Worker w2 = new Worker();
        b.setWorker(w2, destinationPosition);
        b.putWorkers(startPosition, destinationPosition, pushDestPosition);
        assertTrue(w2.getCurrentPosition().equals(startPosition));
        assertTrue(w1.getCurrentPosition().equals(destinationPosition));
    }

    @Test
    void normalMovePutWorkers() throws Exception{
        Position startPosition = new Position(3,2);
        Position destinationPosition = new Position(2,2);
        Worker w1 = b.getBoardCell(startPosition).getWorker();
        b.putWorkers(startPosition, destinationPosition, null);
        assertEquals(null, b.getBoardCell(startPosition).getWorker());
        assertNotEquals(null, b.getBoardCell(destinationPosition).getWorker());
    }

    @Test
    void cloneTest() throws Exception{
        Position p = new Position(3,2);
        Board b2 = b.clone();
        //TODO
        assertNotSame(b.getBoardCell(p), b2.getBoardCell(p));
    }

}