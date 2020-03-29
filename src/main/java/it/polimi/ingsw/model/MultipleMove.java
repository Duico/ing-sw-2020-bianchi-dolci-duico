package it.polimi.ingsw.model;

/**
 * Class that implements the multiples moves strategy of the MoveStrategy
 */

public class MultipleMove implements MoveStrategy {

    private int numMoves;

    public int getNumMoves() {
        return numMoves;
    }

    public MultipleMove(int numMoves){
        this.numMoves = numMoves;
    }

    public MultipleMove(){
        this.numMoves = 1;
    }

    /**
     * Control if the movement is correct based on the multiple move power
     *
     * @param worker Current worker
     * @param cell   destination cell
     * @return
     */
    @Override
    public boolean isValidMove(Worker worker, BoardCell cell) {
        int moveCount = worker.getTurn().getNumMoves();
        if (moveCount==0 && worker.getTurn().getInitialCell() == cell) {
            return false;
        }
        else if(moveCount > 0 && worker.getTurn().getLastMove() == cell){
            return false;
        }else{
            MoveStrategy defaultStrategy = new DefaultMove();
            return defaultStrategy.isValidMove(worker, cell);

        }
    }

    /**
     * Control if the movement is allowed based on the multipleMove's rules
     *
     * @param worker Current worker
     * @return
     */
    @Override
    public boolean isAllowedToMove(Worker worker) {
        if (worker.getTurn().getNumMoves() >= numMoves) {
            return false;
        } else {
            return true;
        }
    }

}