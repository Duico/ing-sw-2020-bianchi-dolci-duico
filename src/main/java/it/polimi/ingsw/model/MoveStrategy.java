package it.polimi.ingsw.model;

public interface MoveStrategy { //implement DefaultMove functions here

    boolean isValidMove(Worker worker, BoardCell cell);
    default boolean isAllowedToMove(Worker worker){
        if(worker.getTurn().getNumMoves() != 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     *
     * @param worker
     * @return True if the worker must build before the end of the current turn
     */
    default boolean isRequiredToMove(Worker worker){
        if(worker.getTurn().getNumMoves() != 0){
            return false;
        }else{
            return true;
        }

    }

}
