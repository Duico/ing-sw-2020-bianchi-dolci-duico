package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the multiples moves strategy of the MoveStrategy
 */

public class MultipleMove implements MoveStrategy {

    private int numMovesAllowed;


    public MultipleMove(int numMovesAllowed){
        this.numMovesAllowed = numMovesAllowed;
    }

    public MultipleMove(){
        this.numMovesAllowed = 2;
    }

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, Board board) {

        BoardCell startCell = board.getBoardCell(startPosition);
        int numMoves = startCell.getWorker().getNumMoves();
        if(numMoves >= 1 && startCell.getWorker().getFirstMove().equals(destPosition)){
            return false;
        }else{
            MoveStrategy defaultStrategy = new DefaultMove();
            return defaultStrategy.isValidMove(startPosition,destPosition,board);
        }
    }

    @Override
    public boolean isAllowedToMove(int numMoves, int numBuilds) {
        if (numMoves >= (numMovesAllowed) || numBuilds>0) {
            return false;
        } else {
            return true;
        }
    }

}