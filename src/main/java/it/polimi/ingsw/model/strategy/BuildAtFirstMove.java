package it.polimi.ingsw.model.strategy;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.BoardCell;
import it.polimi.ingsw.model.Position;

/**
 * Class that implements the movement when you can build before move
 */

public class BuildAtFirstMove implements MoveStrategy {

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, Board board) {

        try{

            int dx = destPosition.getX() - startPosition.getX();
            int dy = destPosition.getY() - startPosition.getY();
            BoardCell destCell = board.getBoardCell(destPosition);
            BoardCell startCell = board.getBoardCell(startPosition);

            if(dx == 0 && dy == 0)
                return false;
            else if (destCell.getWorker() != null)
                return false;
            else if (dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if(destCell.hasDome() == true)
                return false;

            else if (startCell.getWorker().getNumBuilds() == 0) {
                if ((startCell.getLevel().getOrd()+1) >= destCell.getLevel().getOrd())
                    return true;
                else
                    return false;
            } else {
                if (startCell.getLevel().getOrd() >= destCell.getLevel().getOrd())
                    return true;
                else
                    return false;
            }
        }catch (NullPointerException e ){
            throw new NullPointerException();
        }

    }

}
