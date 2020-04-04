package it.polimi.ingsw.model;

/**
 * Class that implements the multiples moves strategy of the MoveStrategy
 */

public class MultipleMove implements MoveStrategy {

    private int numMovesAllowed;


    public MultipleMove(int numMovesAllowed){
        this.numMovesAllowed = numMovesAllowed;
    }

    public MultipleMove(){
        this.numMovesAllowed = 1;
    }

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, BoardCell[][] grid) {

        BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
        int numMoves = startCell.getWorker().getNumMoves();
        if(numMoves > 1 && startCell.getWorker().getFirstMove().equals(destPosition)){
            return false;
        }else{
            MoveStrategy defaultStrategy = new DefaultMove();
            return defaultStrategy.isValidMove(startPosition,destPosition,grid);
        }
    }

    @Override
    public boolean isAllowedToMove(int numMoves) {
        if (numMoves >= (numMovesAllowed)) {
            return false;
        } else {
            return true;
        }
    }

}