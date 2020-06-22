package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the power to move in a occupied cell
 */
public class PushMove implements MoveStrategy {

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, Board board) {

        try {

            BoardCell startCell = board.getBoardCell(startPosition);
            BoardCell destCell = board.getBoardCell(destPosition);
            int dx = destPosition.getX() - startPosition.getX();
            int dy = destPosition.getY() - startPosition.getY();
            if(dx==0 && dy==0)
                return false;
            else if(dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if(destCell.hasDome())
                return false;
            else if((startCell.getLevel().getOrd()+1) >= (destCell.getLevel().getOrd())) {
                return true;
            }else
                return false;
        }catch (NullPointerException e){
            throw new NullPointerException();
        }

    }

}
