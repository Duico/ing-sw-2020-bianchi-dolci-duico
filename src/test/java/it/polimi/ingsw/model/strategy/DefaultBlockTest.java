package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBlockTest {

    static Board board;
    static BlockStrategy defaultBlock = new DefaultBlock();


    /**
     * Setup before all the test
     */
    @BeforeEach
    public void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");

        Position.setSize(5,5);
        board = new Board();

    }

    /**
     * Return always false
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isBlockMove() throws PositionOutOfBoundsException {
        Position startPosition = new Position(0,0);
        Position destPosition = new Position(1,1);
        assertFalse(defaultBlock.isBlockMove(startPosition, destPosition, board));
    }

    /**
     * Return always false
     * @throws PositionOutOfBoundsException
     */
    @Test
    void blockNextPlayer() throws PositionOutOfBoundsException {
        Position startPosition = new Position(0,0);
        Position destPosition = new Position(1,1);
        assertFalse(defaultBlock.blockNextPlayer(startPosition, destPosition, board));
    }


}