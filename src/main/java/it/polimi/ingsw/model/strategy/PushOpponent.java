package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.*;

/**
 * Class that implements the power to move in a occupied cell and push the opponent worker
 */

public class PushOpponent implements OpponentStrategy {

    @Override
    public boolean isValidPush(Position startPosition, Position destPosition, boolean isOwnWorker, Board board){  //domandare come passare matrice
        if(isOwnWorker) return false;

        BoardCell destCell = board.getBoardCell(destPosition);

        if(destCell.getWorker() == null) return true;
        else {
            try{
            Position pushDestPosition;
            pushDestPosition = this.destinationPosition(startPosition, destPosition);
            BoardCell pushDestCell = board.getBoardCell(pushDestPosition);
                if( pushDestCell.getWorker() != null ) return false;
                else if( pushDestCell.hasDome()) return false;
                else{
                    return true;
                }
            }catch (InvalidPushCell e){
                return false;
            }


        }
    }


    @Override
    public Position destinationPosition(Position startPosition, Position destPosition) throws InvalidPushCell{
        int dx = destPosition.getX() - startPosition.getX();
        int dy = destPosition.getY() - startPosition.getY();
        if(dx > 1 || dy > 1) throw new InvalidPushCell();

        int x = destPosition.getX() + dx;
        int y = destPosition.getY() + dy;
        try{
            Position pos = new Position(x,y);
            return pos;
        }catch (PositionOutOfBoundsException e){
            throw new InvalidPushCell();
        }
    }
}

