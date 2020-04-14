package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import it.polimi.ingsw.model.strategy.*;
import static org.junit.jupiter.api.Assertions.*;

class DefaultMoveTest {

    static Board board;
    static Game game;
    static MoveStrategy defaultMove;

    /**
     * Setup before all the test
     */
    @BeforeAll
    public static void before() throws PositionOutOfBoundsException {
        System.out.println("Running setup");

        try {
            Player player1 = new Player("Pippo");

            Player player2 = new Player("Pluto");
            ArrayList<Player> players = new ArrayList<Player>();
            players.add(player1);
            players.add(player2);
            Game game = new Game();
            game.startGame(players, false);
            System.out.println(game.getTurn().getCurrentPlayer().getNickName());
            System.out.println(game.getTurn().getCurrentPlayer().getCard().getName());
            Position startPosition1 = new Position(0,0);
            Position startPosition2 = new Position(1,1);
            game.place(startPosition1);
            game.place(startPosition2);


        }catch (Exception e){
            System.err.println("erorre");
            e.printStackTrace();
        }


    }


    /**
     * Control if return true when the position of destination is right
     * @throws PositionOutOfBoundsException
     */
    @Test
    void validPositionOfTheMove() throws PositionOutOfBoundsException {

        Position startPosition = game.getTurn().getCurrentPlayer().getWorkerCurrentPosition(1);
        Position destPosition = new Position(0,1);
        assertTrue(defaultMove.isValidMove(startPosition, destPosition, board));
    }


   /* *//**
     * Control if return false when the destination position is wrong
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void notValidPositionOfTheMove() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(4, 4);
        BoardCell[][] grid = board.getGrid();
        assertFalse(defaultMove.isValidMove(startPosition, destPosition, grid));
    }


    *//**
     * Control if return true when the destination cell is right based on the level
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void validLevel() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.BASE);
        assertTrue(defaultMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
    }

    *//**
     * Control if return true when the destination cell is right based on the right destination level
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void validLevelMoveDown() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.MID);
        assertTrue(defaultMove.isValidMove(startPosition, destPosition, grid));
        grid[startPosition.getX()][startPosition.getY()].setLevel(Level.EMPTY);
    }


    *//**
     * Control if return false when the destination cell is wrong based on the wrong level
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void notValidLevel() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.MID);
        assertFalse(defaultMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
    }


    *//**
     * Control if return false when the destination cell is wrong because it has the dome
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void notValidBecauseDome() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(1, 1);
        BoardCell[][] grid = board.getGrid();
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.MID);
        grid[destPosition.getX()][destPosition.getY()].setDome(true);
        assertFalse(defaultMove.isValidMove(startPosition, destPosition, grid));
        grid[destPosition.getX()][destPosition.getY()].setLevel(Level.EMPTY);
        grid[destPosition.getX()][destPosition.getY()].setDome(false);
    }


    *//**
     * Control if return false when the destination cell is the same of the start cell
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void notValidBecauseSameStartCell() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(2, 2);
        BoardCell[][] grid = board.getGrid();
        assertFalse(defaultMove.isValidMove(startPosition, destPosition, grid));
    }


    *//**
     * Control if return false when the destination cell is occupied by a worker
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void notValidBecauseOccupiedCell() throws PositionOutOfBoundsException {
        Position startPosition = new Position(2,2);
        Position destPosition = new Position(3, 3);
        BoardCell[][] grid = board.getGrid();
        assertFalse(defaultMove.isValidMove(startPosition, destPosition, grid));
    }


    *//**
     * Control if return true when the worker can move
     *//*
    @Test
    void isAllowedToMove() {
        int numMoves = worker1.getNumMoves();
        assertTrue(defaultMove.isAllowedToMove(numMoves));
    }


    *//**
     * Control if return true when the worker must move
     *//*
    @Test
    void isRequiredToMove() {
        int numMoves = worker1.getNumMoves();
        assertTrue(defaultMove.isAllowedToMove(numMoves));
    }


    *//**
     * Control if return false when the worker can't move
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void isNotAllowedToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,1);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        assertFalse(defaultMove.isAllowedToMove(numMoves));
        worker1.deleteLastMove();
    }


    *//**
     * Control if return false when the worker has already move the first time
     * @throws PositionOutOfBoundsException
     *//*
    @Test
    void isNotRequiredToMove() throws PositionOutOfBoundsException {
        Position newPosition = new Position(1,1);
        worker1.addMove(newPosition);
        int numMoves = worker1.getNumMoves();
        assertFalse(defaultMove.isRequiredToMove(numMoves));
        worker1.deleteLastMove();
    }
*/

}