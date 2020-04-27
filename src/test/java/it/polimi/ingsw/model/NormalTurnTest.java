package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/*
class NormalTurnTest {

    static Board board;
    static Card card;
    static Card card2;
    static NormalTurn turn;
    static NormalTurn turn2;
    static Player player1;
    static Player player2;
    static Worker worker1;
    static Worker worker2;
    static Worker worker3;

    @BeforeEach
    void before() throws StrategyNameNotFound {
        card = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        card2 = new Card("Athena", "Default", "Default", "Default", "UpMove", "Default");
        player1 = new Player("player1");
        player2= new Player("player2");
        turn = new NormalTurn(player1, card, false);
        turn2 = new NormalTurn(player1, card2, true);
        board = new Board();
        worker3=new Worker();

    }

    public void setWorkerOnTheBoard() throws PositionOutOfBoundsException {

        Position pos0 = new Position(0, 0);
        Position pos1 = new Position(3, 3);
        Position pos2 = new Position(1,1);
        board.setWorker(worker1, pos0);
        board.setWorker(worker2, pos1);
        board.setWorker(worker3, pos2);
    }

    public void setPlayer1DefaultCard(){
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        player1.setCard(card);
    }

    public void setPlayer1MinotauroCard() throws StrategyNameNotFound {
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        Card card = new Card("Minotaur", "Push", "Default", "Default", "Default", "Push");
        player1.setCard(card);
    }

    public void setPlayerBlockCard() throws StrategyNameNotFound {
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        Card card = new Card("Minotaur", "Push", "Default", "Default", "Default", "Push");
        player1.setCard(card);
    }

    @Test
    void checkCorrectTurnPhase() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertEquals(TurnPhase.NORMAL, turn.getPhase());
    }

    @Test
    void checkCurrentWorker() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertFalse(turn.isSetCurrentWorker());

        // !!
        // usa
        // assertEquals(turn.getCurrentWorkerId(), 0)
        assertTrue(turn.checkCurrentWorker(0));
        assertTrue(turn.checkCurrentWorker(1));
        assertEquals(-1, turn.getCurrentWorkerId());
        turn.updateCurrentWorker(0);
        assertTrue(turn.isSetCurrentWorker());
        assertTrue(turn.checkCurrentWorker(0));
        assertFalse(turn.checkCurrentWorker(1));
        assertEquals(0, turn.getCurrentWorkerId());
    }

    @Test
    void checkIsAllowedToMove() throws WorkerPositionNotSetException, PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.isAllowedToMove(0));
        assertTrue(turn.isAllowedToMove(1));
    }

    @Test
    void checkIsNotAllowedToMove() throws WorkerPositionNotSetException, PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position position = new Position(0,1);
        worker1.addMove(position);
        assertFalse(turn.isAllowedToMove());
    }

    @Test
    void checkIsRequiredToMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.isRequiredToMove(0));
        assertTrue(turn.isRequiredToMove(1));
        turn.updateCurrentWorker(0);
        assertTrue(turn.isRequiredToMove());
    }

    @Test
    void checkIsNotRequiredToMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertFalse(turn.isRequiredToMove());
    }

    @Test
    void checkIsRequiredToBuild() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.isRequiredToBuild(0));
        assertTrue(turn.isRequiredToBuild(1));
        turn.updateCurrentWorker(0);
        assertTrue(turn.isRequiredToBuild());
    }

    @Test
    void checkIsRequiredToBuild2() throws PositionOutOfBoundsException {
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertTrue(turn.isRequiredToBuild());
    }

    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertTrue(turn.isAllowedToBuild(0));
        assertFalse(turn.isAllowedToBuild(1));
        assertTrue(turn.isAllowedToBuild());
    }

    @Test
    void checkIsNotAllowedToBuild() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(0,0);
        worker1.addMove(pos1);
        worker1.addBuild(pos2);
        assertFalse(turn.isAllowedToBuild());
    }

    @Test
    void checkPlayer(){
        assertTrue(turn.isCurrentPlayerUUID(player1.getUuid()));
        assertFalse(turn.isCurrentPlayerUUID(player2.getUuid()));
    }

    @Test
    void isBlockNextPlayer(){
        assertFalse(turn.isBlockNextPlayer());
        assertFalse(turn.getPreviousBlockNextPlayer());
    }

    @Test
    void isFeasibleMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(0,0);
        assertTrue(turn.isFeasibleMove(board,0, pos1));
        assertFalse(turn.isFeasibleMove(board,0, pos2));

    }

    @Test
    void move() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        turn.boardMove(board,0, pos1);
        assertEquals(pos1, player1.getWorkerPosition(0));
        assertEquals(Operation.MOVE, worker1.getLastOperation());
    }

    @Test
    void isFeasibleMoveWithMinotauro() throws PositionOutOfBoundsException, StrategyNameNotFound {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1, 1);
        assertTrue(turn.isFeasibleMove(board,0, pos1));
    }

    @Test
    void moveMinotauro() throws PositionOutOfBoundsException, WorkerPositionNotSetException, StrategyNameNotFound {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1, 1);
        assertTrue(turn.isFeasibleMove(board,0, pos1));
        assertTrue(worker3.getCurrentPosition().equals(pos1));
        turn.boardMove(board,0, pos1);
        assertEquals(pos1, player1.getWorkerPosition(0));
        assertEquals(Operation.MOVE, worker1.getLastOperation());
        assertTrue(worker3.getCurrentPosition().equals(new Position(2,2)));
    }

    @Test
    void isNotFeasibleMove() throws PositionOutOfBoundsException, StrategyNameNotFound {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0,0);
        assertFalse(turn.isFeasibleMove(board,1, pos1));
    }

    @Test
    void isFeasibleBuild() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        assertTrue(turn.isFeasibleBuild(board,0, pos1, false));

    }


    @Test
    void isNotFeasibleBuild() throws PositionOutOfBoundsException, StrategyNameNotFound {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1,1);
        assertFalse(turn.isFeasibleBuild(board,1, pos1, false));
    }

    @Test
    void build() throws PositionOutOfBoundsException, StrategyNameNotFound, WorkerPositionNotSetException {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        turn.boardBuild(board,0, pos1, false);
        assertEquals(Level.BASE, board.getBoardCell(pos1).getLevel());

    }

    @Test
    void blockMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2= new Position(1,0);
        turn2.boardBuild(board,0, pos1, false);
        assertTrue(turn2.isBlockedMove(board, 0, pos1));
        assertFalse(turn2.isBlockedMove(board, 0, pos2));
    }

    @Test
    void isWinningMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0,1);
        assertFalse(turn.isWinningMove(board,pos1,0));
    }

    @Test
    void canMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.canMove(board, 0));
        assertTrue(turn.canMove(board, 1));

    }

    @Test
    void cantMove() throws PositionOutOfBoundsException {
        BoardCantMove();
        assertFalse(turn.canMove(board, 0));
    }

    @Test
    void canBuild() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.canBuild(board, 0));
        assertTrue(turn.canBuild(board, 1));
    }

    @Test
    void isLoseCondition() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertFalse(turn.isLoseCondition(board));
    }

    @Test
    void isLoseCondition2() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        turn.boardMove(board,0, pos1);
        assertFalse(turn.isLoseCondition(board));
    }

    @Test
    void isLoseCondition3() throws PositionOutOfBoundsException {
        BoardCantMove();
        assertTrue(turn.isLoseCondition(board));
    }

    void BoardCantMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1,1);
        Position pos3 = new Position(1,0);
        turn.boardBuild(board,0, pos1, true);
        turn.boardBuild(board,0, pos2, true);
        turn.boardBuild(board,0, pos3, true);
    }







}

 */