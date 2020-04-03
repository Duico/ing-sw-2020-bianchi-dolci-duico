package it.polimi.ingsw.model;

/**
 * Class that implements the power to move in a occupied cell and push the opponent worker
 */

public class PushOpponent implements OpponentStrategy {
    /**
     * return if that worker can push the opponent worker
     * @param worker Current worker
     * @param cell   Destination cell for current worker
     * @return
     */
    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, BoardCell[][] grid){  //domandare come passare matrice

        Worker opponentWorker = grid[destPosition.getX()][destPosition.getY()].getWorker();
        boolean isFree = opponentWorker == null;

        if(isFree) return true;

        else {
            try{
            Position pushDestPosition;
            //fare le variabili
            pushDestPosition = this.destinationPosition(startPosition, destPosition);
                if( grid[pushDestPosition.getX()][pushDestPosition.getY()].getWorker() != null ) return false;
                    //destCell has dome on top
                else if( grid[pushDestPosition.getX()][pushDestPosition.getY()].hasDome()) return false;
                else{
                    return true;
                }
            }catch (PositionOutOfBoundsException e){
                return false;
            }catch (InvalidPushCell e){
                //TODO consider throwing exception
                return false;
            }


        }
    }

    /**
     * push the opponent worker in the correct destination cell based of the correct power of the card
     * @param worker Current worker
     * @param cell Destination cell for current worker
     * @throws InvalidPushCell
     * @throws PositionOutOfBoundsException
     */

    // non serve più ora
    @Override
    public void pushOpponent(Worker worker, BoardCell cell){
        //if( this.isValidPush(worker, cell) )

        BoardCell startCell = worker.getCell();
        Worker opponentWorker = cell.getWorker();

        if(opponentWorker == null) return;
        try {
            Position destPosition = this.destinationPosition(startCell, cell);
            BoardCell destCell = worker.getTurn().getBoard().getBoardCell(destPosition);
            destCell.setWorker(opponentWorker);
            opponentWorker.setCell(destCell);
            cell.setWorker(null);

        }catch (PositionOutOfBoundsException e){
            return;
        }catch (InvalidPushCell e){
            return;
            //TODO consider throwing exception
        }

    }

    /**
     * Calculate the right Destination position of the opponent worker
     * @param startCell
     * @param cell
     * @return
     * @throws InvalidPushCell
     * @throws PositionOutOfBoundsException
     */
    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell, PositionOutOfBoundsException{
        int dx = destPosition.getX() - startPosition.getX();
        int dy = destPosition.getY() - startPosition.getY();
        if(dx > 1 || dy > 1) throw new InvalidPushCell();

        int x = destPosition.getX() + dx;
        int y = destPosition.getY() + dy;

        return new Position(x,y);
    }
}

