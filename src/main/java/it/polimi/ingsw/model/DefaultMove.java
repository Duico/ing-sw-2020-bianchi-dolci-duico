package it.polimi.ingsw.model;

/**
 * Class that implements the default strategy of the MoveStrategy
 */

public class DefaultMove implements MoveStrategy { //inherit functions from Interface
    /**
     * Control if the movement is correct based on the default game's rules of movements
     * @param worker Current worker
     * @param cell Destination cell
     * @return
     */
    @Override
    public boolean isValidMove(Worker worker, BoardCell cell) {

        MoveStrategy pushStrategy = new PushMove();
        return pushStrategy.isValidMove(worker, cell) && (cell.getWorker() == null);
        // possibility of exception?
    }


}
