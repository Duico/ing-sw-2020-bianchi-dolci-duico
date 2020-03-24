package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlaceWorkersTurnTest {

    static Board board;
    static Card card;
    static PlaceWorkersTurn turn;
    static Player player1;

    @BeforeEach
    void before() throws StrategyNameNotFound, PositionOutOfBoundsException {
        card = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        player1 = new Player("player1");
        turn = new PlaceWorkersTurn(player1);
        board = new Board();
        Position pos0 = new Position(0, 0);
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1, 0);
        player1.initWorkers(2);
        player1.setCard(card);
    }

    /**
     * check if worker is placed correctly
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkBoardPlace() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        assertTrue(turn.boardPlace(board, pos1)==0);
    }

    /**
     * check worker not placed when destination position is already occupied by a worker
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkBoardPlace2() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        turn.boardPlace(board, pos1);
        assertTrue(turn.boardPlace(board, pos1)==-1);
    }

    /**
     * check if second worker is placed correctly
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkBoardPlace3() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1, 0);
        turn.boardPlace(board, pos1);
        assertTrue(turn.boardPlace(board, pos2)==1);
    }

    /**
     * check if no more than two workers can be placed
     * because of currentPlayer.initWorkers(2);
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkBoardPlace4() throws PositionOutOfBoundsException {
        Position pos0 = new Position(0, 0);
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1, 0);
        turn.boardPlace(board, pos1);
        turn.boardPlace(board, pos2);
        assertTrue(turn.boardPlace(board, pos0)==-1);
    }


}