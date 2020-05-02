package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardCellTest {
    BoardCell bc;
    Worker w;

    @BeforeEach
    void setUp() {
        bc = new BoardCell();
        w = new Worker();
        try{
            w.addMove(new Position(2,3));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    void levelFullTest() {

        assertEquals(bc.getLevel(), Level.EMPTY);
        bc.setLevel(Level.BASE);

        assertEquals(bc.getLevel(), Level.BASE);
        bc.setLevel(Level.MID);

        assertEquals(bc.getLevel(), Level.MID);
        bc.setLevel(Level.TOP);

        assertEquals(bc.getLevel(), Level.TOP);
        bc.setDome(true);

        assertEquals(bc.getLevel(), Level.TOP);
    }

    @Test
    void domeTest() {
        assertFalse(bc.hasDome());
        bc.setDome(true);
        assertTrue(bc.hasDome());
    }

    @Test
    void workerTest() {
        bc.setWorker(w);
        assertEquals(bc.getWorker(), w);
        assertTrue(bc.getWorker() == w);
    }

    @Test
    void cloneTest(){
        bc.setWorker(w);
        bc.setLevel(Level.MID);

        BoardCell bcClone = bc.clone();
        assertNotSame(bc, bcClone);
        assertNotSame(bc.getWorker(), bcClone.getWorker());
        assertEquals(bc.getLevel(), bcClone.getLevel());

        assertEquals(bc.hasDome(), bcClone.hasDome());

    }
}