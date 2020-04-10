package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.*;

/**
 * Class that implements the power to move in a occupied cell and push the opponent worker
 */

public class PushOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, BoardCell[][] grid){  //domandare come passare matrice
        if(isOwnWorker) return false;

        Worker opponentWorker = grid[destPosition.getX()][destPosition.getY()].getWorker();
        boolean isFree = opponentWorker == null;

        if(isFree == true) return true;
        else {
            try{
            Position pushDestPosition;
            pushDestPosition = this.destinationPosition(startPosition, destPosition);
            BoardCell pushDestCell = grid[pushDestPosition.getX()][pushDestPosition.getY()];
                if( pushDestCell.getWorker() != null ) return false;
                else if( pushDestCell.hasDome()) return false;
                else{
                    return true;
                }
            }catch (PositionOutOfBoundsException e){
                return false;
            }catch (InvalidPushCell e){
                return false;
            }


        }
    }


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

