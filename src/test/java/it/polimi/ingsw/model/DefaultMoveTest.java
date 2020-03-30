package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class DefaultMoveTest {

    static Game partita;
    static Worker startWorker;
    static Worker opponentWorker;

    /**
     * Setup before all the test
     */
    @BeforeAll
    public static void before() {
        System.out.println("Running setup");
        ArrayList<String> nickNames = new ArrayList<String>();
        nickNames.add("Player1");
        nickNames.add("Player2");
        partita = Game.createGame(nickNames, true);
        Card carta = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        Card carta2 = new Card("Default", "Default", "Default", "Default", "Default", "Default");
        partita.getPlayer(0).setCard(carta);
        partita.getPlayer(1).setCard(carta2);


        try {
            Turn previousTurn = new Turn(partita.getPlayer(1));
            partita.setTurn(previousTurn);
            startWorker = partita.getPlayer(0).getWorker(0);
            Position startPosition = new Position(2, 2);
            BoardCell startWorkerCell = new BoardCell(startPosition);
            startWorker.placeWorker(startWorkerCell);
            startWorker.setTurn();


            Turn currentTurn = new Turn(partita.getPlayer(0));
            partita.setTurn(currentTurn);
            opponentWorker = partita.getPlayer(1).getWorker(0);
            Position opponentPosition = new Position(1, 1);
            BoardCell opponentWorkerCell = new BoardCell(opponentPosition);
            opponentWorker.placeWorker(opponentWorkerCell);
            opponentWorker.setTurn();
        }catch (Exception e){
            System.err.println("Errore");
        }

    }

    /**
     * Control if return true when the position of destination is right
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(0));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        assertTrue(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));

    }

    /**
     * Control if return false when the destination position is wrong
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        assertFalse(opponentWorker.getCard().getMoveStrategy().isValidMove(opponentWorker, destCell));

    }

    /**
     * Control if return true when the destination cell is right based on the level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validLevel() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        destCell.setLevel(Level.BASE);
        assertTrue(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));
    }

    @Test
    void validLevelMoveDown() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        startWorker.getCell().setLevel(Level.MID);
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        destCell.setLevel(Level.BASE);
        assertTrue(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));
        startWorker.getCell().setLevel(Level.EMPTY);

    }

    /**
     * Control if return false when the destination cell is wrong based on the wrong level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidLevel() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        destCell.setLevel(Level.MID);
        assertFalse(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));
    }

    /**
     * Control if return false when the destination cell is wrong because it has the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseDome() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(3, 3);
        BoardCell destCell = new BoardCell(destPosition);
        destCell.setLevel(Level.BASE);
        destCell.setDome(true);
        assertFalse(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));

    }

    /**
     * Control if return false when the destination cell is the same of the start cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseSameStartCell() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(2, 2);
        BoardCell destCell = new BoardCell(destPosition);
        assertFalse(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));

    }

    /**
     * Control if return false when the destination cell is occupied by a worker
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        BoardCell destCell = opponentWorker.getCell();
        assertFalse(startWorker.getCard().getMoveStrategy().isValidMove(startWorker, destCell));
    }

    /**
     * Control if return true when the worker can move
     */
    @Test
    void isAllowedToMove() {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        assertTrue(startWorker.getCard().getMoveStrategy().isAllowedToMove(startWorker));
    }

    /**
     * Control if return false when the worker can't move
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotAllowedToMove() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(1, 2);
        BoardCell firstMoveCell = new BoardCell(destPosition);
        newTurn.addMovePosition(firstMoveCell);
        assertFalse(startWorker.getCard().getMoveStrategy().isAllowedToMove(startWorker));
    }

    /**
     * Control if return true when the worker must move
     */
    @Test
    void isRequiredToMove() {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        assertTrue(startWorker.getCard().getMoveStrategy().isRequiredToMove(startWorker));
    }

    /**
     * Control if return false when the worker has already move the first time
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotRequiredToMove() throws PositionOutOfBoundsException {
        Turn newTurn = new Turn(partita.getPlayer(1));
        partita.setTurn(newTurn);
        startWorker.setTurn();
        Position destPosition = new Position(1, 2);
        BoardCell firstMoveCell = new BoardCell(destPosition);
        newTurn.addMovePosition(firstMoveCell);
        assertFalse(startWorker.getCard().getMoveStrategy().isRequiredToMove(startWorker));
    }



}