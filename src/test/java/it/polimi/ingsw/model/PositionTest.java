package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void setSize() {
        Position.setSize(5,5);
        assertEquals(5, Position.height);
        assertEquals(5, Position.width);
    }
    @Test
    void constructorTest() throws Exception{
        Position.setSize(5,5);
        Position p = new Position(3,4);
        assertEquals(3, p.getX());
        assertEquals(4, p.getY());
    }
    @Test
    void constructorException(){
        Position.setSize(5,5);
        assertThrows(PositionOutOfBoundsException.class, () -> {
           new Position(6,7);
        });
    }
}