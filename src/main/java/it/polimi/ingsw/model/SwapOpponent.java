package it.polimi.ingsw.model;

/**
 * Class that implements the power to move in an occupied cell and swap the opponent worker
 */

public class SwapOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, BoardCell[][] grid) {

        return true;
    }

    //non serve più
    @Override
    public void pushOpponent(Worker worker, BoardCell cell) {

        try {
            Worker opponentWorker = cell.getWorker();  // worker that would be swap
            if (opponentWorker == null) return;
            //FIX
            BoardCell startCell = worker.getCell(); //cell of the start worker
            startCell.setWorker(opponentWorker);  //set the swap worker in the start cell
            //FIX
            opponentWorker.setCell(startCell);   // set in the swap worker the start cell
            cell.setWorker(null);   //set the destination cell to null
        }catch (NullPointerException e){
            throw new NullPointerException();
        }
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException {
        return startPosition;
    }
}
