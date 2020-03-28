package it.polimi.ingsw.model;

public class PushOpponent implements OpponentStrategy {
    @Override
    public boolean isValidPush(Worker worker, BoardCell cell){
        BoardCell startCell = worker.getCell();
        Worker opponentWorker = cell.getWorker();
        boolean isFree = opponentWorker == null;

        if(isFree) return true;
        else {

            Position destPosition;
            try {
                destPosition = this.destinationPosition(startCell, cell);
            }catch (PositionOutOfBoundsException e){
                return false;
            }catch (InvalidPushCell e){
                //TODO consider throwing exception
                return false;
            }
            BoardCell destCell = worker.getTurn().getBoard().getBoardCell(destPosition);
            //destCell occupied by another worker
            if( destCell.getWorker() != null ) return false;
            //destCell has dome on top
            else if(destCell.hasDome()) return false;
            else{
                return true;
            }
        }
    }

    @Override
    public void pushOpponent(Worker worker, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException{
        //if( this.isValidPush(worker, cell) )

        BoardCell startCell = worker.getCell();
        Worker opponentWorker = cell.getWorker();

        if(opponentWorker == null) return;

        Position destPosition = this.destinationPosition(startCell, cell);
        BoardCell destCell = worker.getTurn().getBoard().getBoardCell(destPosition);

        destCell.setWorker(opponentWorker);
        opponentWorker.setCell(destCell);
        cell.setWorker(null);



    }
    @Override
    public Position destinationPosition(BoardCell startCell, BoardCell cell) throws InvalidPushCell, PositionOutOfBoundsException{
        Position pushPosition;
        int width = 5;
        int height = 5;
        // (cellX - startX)
        int dx = cell.getPosition().getX() - startCell.getPosition().getX();
        int dy = cell.getPosition().getY() - startCell.getPosition().getY();
        if(dx > 1 || dy > 1) throw new InvalidPushCell();

        int x = cell.getPosition().getX() + dx;
        int y = cell.getPosition().getY() + dy;

        Position destPosition;
        return new Position(x,y);
    }
}

