package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.exception.StrategyNameNotFound;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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

    /**
     * Create two players and two cards. Create two normal turn
     * @throws StrategyNameNotFound
     */
    @BeforeEach
    void before() throws StrategyNameNotFound {
        card = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        card2 = new Card("Athena", "Default", "Default", "Default", "UpMove", "Default");
        player1 = new Player("player1");
        player2= new Player("player2");
        turn = new NormalTurn(player1, card, false);
        // turn 2: the movement of the worker block the next player
        turn2 = new NormalTurn(player1, card2, true);
        board = new Board();
        worker3=new Worker();

    }

    /**
     * Setting the position of three workers on the board
     * @throws PositionOutOfBoundsException
     */
    public void setWorkerOnTheBoard() throws PositionOutOfBoundsException {

        Position pos0 = new Position(0, 0);
        Position pos1 = new Position(3, 3);
        Position pos2 = new Position(1,1);
        board.setWorker(worker1, pos0);
        board.setWorker(worker2, pos1);
        board.setWorker(worker3, pos2);
    }

    /**
     * Setting for the player 1 the card Default and the worker1 and the worker2
     */
    public void setPlayer1DefaultCard(){
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        player1.setCard(card);
    }

    /**
     * Setting for the player1 the minotauro card and the two workers
     * @throws StrategyNameNotFound
     */
    public void setPlayer1MinotauroCard() throws StrategyNameNotFound {
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        Card card = new Card("Minotaur", "Push", "Default", "Default", "Default", "Push");
        player1.setCard(card);
    }

    //TODO: delete
    public void setPlayerBlockCard() throws StrategyNameNotFound {
        worker1 = new Worker();
        worker2 = new Worker();
        player1.initWorkers(2);
        player1.addWorker(worker1);
        player1.addWorker(worker2);
        Card card = new Card("Minotaur", "Push", "Default", "Default", "Default", "Push");
        player1.setCard(card);
    }

    /**
     * Check if when we play without card, the turn is a normalTurn
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkCorrectTurnPhase() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertEquals(TurnPhase.NORMAL, turn.getPhase());
    }


    /**
     * Check the checkCurrentWorker function
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void checkCurrentWorker() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        //initial phase of the turn, the currentWorkerId is not set
        assertFalse(turn.isSetCurrentWorker());
        assertTrue(turn.checkCurrentWorker(worker1.getCurrentPosition()));
        assertTrue(turn.checkCurrentWorker(worker2.getCurrentPosition()));

        //we update the currentWorkerId
        turn.updateCurrentWorker(0);
        assertTrue(turn.isSetCurrentWorker());
        assertTrue(turn.checkCurrentWorker(worker1.getCurrentPosition()));
        assertFalse(turn.checkCurrentWorker(worker2.getCurrentPosition()));
        assertNotNull(turn.getCurrentWorkerId());
    }

    /**
     * check if are allowed to move the two workers at the start of the turn
     * @throws WorkerPositionNotSetException
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsAllowedToMove() throws WorkerPositionNotSetException, PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        //all the two workers are allowed to move
        assertTrue(turn.isAllowedToMove(worker1.getCurrentPosition()));
        assertTrue(turn.isAllowedToMove(worker2.getCurrentPosition()));
    }

    /**
     * check if the currentWorker is allowed to move when it has already move
     * @throws WorkerPositionNotSetException
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsNotAllowedToMove() throws WorkerPositionNotSetException, PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position position = new Position(0,1);
        //we add a movement to the worker
        worker1.addMove(position);
        //with a default card is not allowed to move
        assertFalse(turn.isAllowedToMove());
    }

    /**
     * check if the two worker are required to move.
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void checkIsRequiredToMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        //control that without a currentWorkerId, all the two workers are required to move
        assertTrue(turn.isRequiredToMove(worker1.getCurrentPosition()));
        assertTrue(turn.isRequiredToMove(worker2.getCurrentPosition()));
        //we update the currentWorkerId
        turn.updateCurrentWorker(0);
        assertTrue(turn.isRequiredToMove());
    }

    /**
     * check that when the worker has moved, is not required to move
     * @throws PositionOutOfBoundsException
     */
    @Test
    void checkIsNotRequiredToMove() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position pos1 = new Position(0, 1);
        //we add a movement at the worker
        worker1.addMove(pos1);
        assertFalse(turn.isRequiredToMove());
    }

    /**
     * Check if the two workers are required to build
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void checkIsRequiredToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        //all the two workers are required to build
        assertTrue(turn.isRequiredToBuild(worker1.getCurrentPosition()));
        assertTrue(turn.isRequiredToBuild(worker2.getCurrentPosition()));
        //when the currentWorkerId is update, only the currentWorkerId is required to move
        turn.updateCurrentWorker(0);
        assertTrue(turn.isRequiredToBuild());
    }

    /**
     * Check if the current worker is allowed to build
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void checkIsAllowedToBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        turn.updateCurrentWorker(0);
        Position pos1 = new Position(0, 1);
        worker1.addMove(pos1);
        assertTrue(turn.isAllowedToBuild(worker1.getCurrentPosition()));
        assertFalse(turn.isAllowedToBuild(worker2.getCurrentPosition()));
        assertTrue(turn.isAllowedToBuild());
    }

    /**
     * we add a build operation and we control that the currentWorkerId is not allowed to build
     * @throws PositionOutOfBoundsException
     */
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

    /**
     * check the currentPlayer of the turn
     */
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

    /**
     * control if is feasible default move
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void isFeasibleMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(0,0);
        //corret position of movement
        assertTrue(turn.isFeasibleMove(board,worker1.getCurrentPosition(), pos1));
        //incorrect position (startposition and destPosition are the same)
        assertFalse(turn.isFeasibleMove(board,worker1.getCurrentPosition(), pos2));

    }

    /**
     * control the corret movement on the board
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     */
    @Test
    void move() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        assertFalse(turn.isSetCurrentWorker());
        turn.boardMove(board,worker1.getCurrentPosition(), pos1);
        assertTrue(turn.isSetCurrentWorker());
        assertNotNull( turn.getCurrentWorkerId());
        assertEquals(pos1, player1.getWorkerPosition(0));
        assertEquals(Operation.MOVE, worker1.getLastOperation());
    }

    /**
     * Control if is feasible move with minotauro card
     * In particular we move in a occupied cell and we control if we can push the opponent worker
     * @throws PositionOutOfBoundsException
     * @throws StrategyNameNotFound
     * @throws WorkerPositionNotSetException
     */
    @Test
    void isFeasibleMoveWithMinotauro() throws PositionOutOfBoundsException, StrategyNameNotFound, WorkerPositionNotSetException {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1, 1);
        assertTrue(turn.isFeasibleMove(board,worker1.getCurrentPosition(), pos1));
    }

    /**
     * we effectly do the push movement of minotauro card.
     * @throws PositionOutOfBoundsException
     * @throws WorkerPositionNotSetException
     * @throws StrategyNameNotFound
     */
    @Test
    void moveMinotauro() throws PositionOutOfBoundsException, WorkerPositionNotSetException, StrategyNameNotFound {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1, 1);
        assertTrue(turn.isFeasibleMove(board,worker1.getCurrentPosition(), pos1));
        assertTrue(worker3.getCurrentPosition().equals(pos1));
        turn.boardMove(board,worker1.getCurrentPosition(), pos1);
        //control the new position of the current worker
        assertEquals(pos1, player1.getWorkerPosition(0));
        assertEquals(Operation.MOVE, worker1.getLastOperation());
        //control the correct destination position of the pushed worker
        assertTrue(worker3.getCurrentPosition().equals(new Position(2,2)));
    }

    /**
     * Not feasible move. However the minotauro card, the destination push cell is out of the board
     * @throws PositionOutOfBoundsException
     * @throws StrategyNameNotFound
     * @throws WorkerPositionNotSetException
     */
    @Test
    void isNotFeasibleMove() throws PositionOutOfBoundsException, StrategyNameNotFound, WorkerPositionNotSetException {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0,0);
        assertFalse(turn.isFeasibleMove(board,worker2.getCurrentPosition(), pos1));
    }


    /**
     * Control if is feasible build with a default card
     */
    @Test
    void isFeasibleBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        assertTrue(turn.isFeasibleBuild(board,worker1.getCurrentPosition(), pos1, false));

    }


    /**
     * Control is not feasible build. (start position and dest position are the same)
     * @throws PositionOutOfBoundsException
     * @throws StrategyNameNotFound
     * @throws WorkerPositionNotSetException
     */
    @Test
    void isNotFeasibleBuild() throws PositionOutOfBoundsException, StrategyNameNotFound, WorkerPositionNotSetException {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(1,1);
        assertFalse(turn.isFeasibleBuild(board,worker2.getCurrentPosition(), pos1, false));
    }

    /**
     * do the operation of build
     * @throws PositionOutOfBoundsException
     * @throws StrategyNameNotFound
     * @throws WorkerPositionNotSetException
     */
    @Test
    void build() throws PositionOutOfBoundsException, StrategyNameNotFound, WorkerPositionNotSetException {
        setPlayer1MinotauroCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        assertFalse(turn.isSetCurrentWorker());
        turn.boardBuild(board,worker1.getCurrentPosition(), pos1, false);
        //control that the worker has built a base construction
        //assertEquals(Level.BASE, board.getBoardCell(pos1).getLevel());
        assertTrue(turn.isSetCurrentWorker());
        assertNotNull(turn.getCurrentWorkerId());

    }

    /**
     * The previous card turn is Athena and the previous player block the next player
     * Control that if the current worker want move up, is block
     */
    @Test
    void blockMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2= new Position(1,0);
        turn2.boardBuild(board,worker1.getCurrentPosition(), pos1, false);
        assertTrue(turn2.isBlockedMove(board, worker1.getCurrentPosition(), pos1));
        assertFalse(turn2.isBlockedMove(board, worker1.getCurrentPosition(), pos2));
    }

    @Test
    void isWinningMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0,1);
        assertFalse(turn.isWinningMove(board,worker1.getCurrentPosition(),pos1));
    }

    @Test
    void canMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertTrue(turn.canMove(board, worker1.getCurrentPosition()));
        assertTrue(turn.canMove(board, worker2.getCurrentPosition()));

    }

    @Test
    void cantMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        BoardCantMove();
        assertFalse(turn.canMove(board, worker1.getCurrentPosition()));
    }

    @Test
    void canBuild() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertFalse(turn.canBuild(board, worker1.getCurrentPosition()));
        assertFalse(turn.canBuild(board, worker2.getCurrentPosition()));
    }

    @Test
    void isLoseCondition() throws PositionOutOfBoundsException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        assertFalse(turn.isLoseCondition(board));
    }

    @Test
    void isLoseCondition2() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        turn.boardMove(board,worker1.getCurrentPosition(), pos1);
        assertFalse(turn.isLoseCondition(board));
    }

    @Test
    void isLoseCondition3() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        BoardCantMove();
        assertTrue(turn.isLoseCondition(board));
    }

    void BoardCantMove() throws PositionOutOfBoundsException, WorkerPositionNotSetException {
        setPlayer1DefaultCard();
        setWorkerOnTheBoard();
        Position pos1 = new Position(0, 1);
        Position pos2 = new Position(1,1);
        Position pos3 = new Position(1,0);
        turn.boardBuild(board,worker1.getCurrentPosition(), pos1, true);
        turn.boardBuild(board,worker1.getCurrentPosition(), pos2, true);
        turn.boardBuild(board,worker1.getCurrentPosition(), pos3, true);
    }

}

