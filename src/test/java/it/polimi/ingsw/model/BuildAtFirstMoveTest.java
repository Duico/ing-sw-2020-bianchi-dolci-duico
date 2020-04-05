package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class Test of BuildAtFirstMove class
 */
class BuildAtFirstMoveTest {
    static Board board;
    static Game game;
    static Worker worker1;
    static Worker worker2;
    static MoveStrategy buildAtFirstMove;

    /**
     * Setup before all the test
     */
    @BeforeAll
    public static void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");

        try {
            ArrayList<String> nickNames = new ArrayList<String>();
            nickNames.add("Player1");
            nickNames.add("Player2");
            game = Game.createGame(nickNames, true);
            buildAtFirstMove = new BuildAtFirstMove();
            board = new Board();
            worker1 = new Worker();
            worker2 = new Worker();
            Position startPositionPlayer1 = new Position(2, 2);
            board.setWorkers(worker1, startPositionPlayer1);
            Position startPositionPlayer2 = new Position(3, 3);
            board.setWorkers(worker2, startPositionPlayer2);


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
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        assertTrue(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
    }


    /**
     * Control if return false when the destination position is wrong
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidPositionOfTheMove() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(4, 4);
        BoardCell[][] grid = board.getGrid();
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
    }


    /**
     * Control if return true when the destination cell is right based on the level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validLevel() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.BASE);
        assertTrue(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
    }

    /**
     * Control if return true when the destination cell is right based on the right destination level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validLevelMoveDown() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.MID);
        assertTrue(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.EMPTY);
    }


    /**
     * Control if return false when the destination cell is wrong based on the wrong level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidLevel() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.MID);
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
    }


    /**
     * Control if return false when the destination cell is wrong because it has the dome
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseDome() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.MID);
        grid[destPosition.getX()][destPosition.getY()].setDome(true);
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
        grid[destPosition.getX()][destPosition.getY()].setDome(false);
    }


    /**
     * Control if return false when the destination cell is the same of the start cell
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseSameStartCell() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(2, 2);
        BoardCell[][] grid = board.getGrid();
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
    }


    /**
     * Control if return false when the destination cell is occupied by a worker
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(3, 3);
        BoardCell[][] grid = board.getGrid();
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
    }

    /**
     * Control if return false when the worker already build and he wants move up
     * @throws PositionOutOfBoundsException
     */
    @Test
    void notValidBecauseAlreadyBuild() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        Position buildPosition = new Position(1,2);
        worker1.addBuild(buildPosition);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.BASE);
        assertFalse(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
        worker1.deleteLastBuild();
    }

    /**
     * Control if return true when the worker already build but he wants move in a cell whit the same start cell level
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validHoweverAlreadyBuild() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        Position buildPosition = new Position(1,2);
        worker1.addBuild(buildPosition);
        BoardCell[][] grid = board.getGrid();
        assertTrue(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        worker1.deleteLastBuild();
    }

    /**
     * Control if return true when the worker already build but he wants to move down
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validHoweverAlreadyBuild2() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        Position buildPosition = new Position(1,2);
        worker1.addBuild(buildPosition);
        BoardCell[][] grid = board.getGrid();
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.BASE);
        assertTrue(buildAtFirstMove.isValidMove(startPosition, destPosition, grid));
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.EMPTY);
        worker1.deleteLastBuild();
    }


    /**
     * Control if return true when the worker can move
     */
    @Test
    void isAllowedToMove() {
        int numMoves = worker1.getNumMoves();
        assertTrue(buildAtFirstMove.isAllowedToMove(numMoves));
    }


    /**
     * Control if return true when the worker must move
     */
    @Test
    void isRequiredToMove() {
        int numMoves = worker1.getNumMoves();
        assertTrue(buildAtFirstMove.isAllowedToMove(numMoves));
    }


    /**
     * Control if return false when the worker can't move
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotAllowedToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,1);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        assertFalse(buildAtFirstMove.isAllowedToMove(numMoves));
        worker1.deleteLastMove();
    }


    /**
     * Control if return false when the worker has already move the first time
     * @throws PositionOutOfBoundsException
     */
    @Test
    void isNotRequiredToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,1);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        assertFalse(buildAtFirstMove.isRequiredToMove(numMoves));
        worker1.deleteLastMove();
    }


}
