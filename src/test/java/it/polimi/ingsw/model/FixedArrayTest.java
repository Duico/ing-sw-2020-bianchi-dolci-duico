package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FixedArrayTest {

    @Test
    void checkEmptyNull(){
        FixedArray<Worker> fa = new FixedArray<>(6);
        for(int i=0; i<6; i++){
            assertNull(fa.get(i));
        }
        assertThrows(IndexOutOfBoundsException.class, ()->{
            fa.get(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, ()->{
            fa.get(6);
        });
        assertThrows(IndexOutOfBoundsException.class, ()->{
            fa.get(3434);
        });
    }
    @Test
    void add() {
        FixedArray<Worker> fa = new FixedArray<>(4);
        Worker w1 = new Worker();
        assertEquals(0, fa.add(w1));
        assertSame(w1, fa.get(0));
        assertNull(fa.get(1));
        assertNull(fa.get(2));
        assertNull(fa.get(3));
        Worker w2 = new Worker();
        assertEquals(1, fa.add(w2));
        assertSame(w1, fa.get(0));
        assertSame(w2, fa.get(1));
        assertNull(fa.get(2));
        assertNull(fa.get(3));
        Worker w3 = new Worker();
        assertEquals(2, fa.add(w3));
        assertSame(w1, fa.get(0));
        assertSame(w2, fa.get(1));
        assertSame(w3, fa.get(2));
        assertNull(fa.get(3));
        Worker w4 = new Worker();
        assertEquals(3, fa.add(w3));
        assertSame(w1, fa.get(0));
        assertSame(w2, fa.get(1));
        assertSame(w3, fa.get(2));
        assertSame(w4, fa.get(3));
        assertEquals(-1, fa.add(w4));

    }

    @Test
    void get() {
    }

    @Test
    void set() {
    }

    @Test
    void size() {
    }
}