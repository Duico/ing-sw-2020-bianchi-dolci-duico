package it.polimi.ingsw.model;

public interface MoveStrategy { //implement DefaultMove functions here
    boolean isValidMove(Worker worker, BoardCell cell);
    boolean isAllowedToMove(Worker worker);

    /**
     *
     * @param worker
     * @return True if the worker must build before the end of the current turn
     */
    boolean isRequiredToMove(Worker worker);

}
