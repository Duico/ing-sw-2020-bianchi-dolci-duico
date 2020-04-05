package it.polimi.ingsw.model;

/**
 * Class that implements the movement when you can build before move
 */

public class BuildAtFirstMove implements MoveStrategy {

    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, BoardCell[][] grid) {

        try{

            int dx = destPosition.getX() - startPosition.getX();
            int dy = destPosition.getY() - startPosition.getY();
            BoardCell destCell = grid[destPosition.getX()][destPosition.getY()];
            BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];

            if(dx == 0 && dy == 0)
                return false;
            else if (destCell.getWorker() != null)
                return false;
            else if (dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if(destCell.hasDome() == true)
                return false;

            else if (startCell.getWorker().getNumBuilds() == 0) {
                if ((startCell.getLevel().ordinal()+1) >= destCell.getLevel().ordinal())
                    return true;
                else
                    return false;
            } else {
                if (startCell.getLevel().ordinal() >= destCell.getLevel().ordinal())
                    return true;
                else
                    return false;
            }
        }catch (NullPointerException e ){
            throw new NullPointerException();
        }

    }

}
