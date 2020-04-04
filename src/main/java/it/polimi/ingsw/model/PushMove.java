package it.polimi.ingsw.model;

//has now also taken the role of SwapMove
public class PushMove implements MoveStrategy {
    @Override
    public boolean isValidMove(Position startPosition, Position destPosition, BoardCell[][] grid) {

        try {

            BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
            BoardCell destCell = grid[destPosition.getY()][destPosition.getY()];
            int dx = destPosition.getX() - startPosition.getX();
            int dy = destPosition.getY() - startPosition.getY();
            if(dx==0 && dy==0)
                return false;
            else if(dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if(destCell.hasDome() == true)
                return false;
            else if((startCell.getLevel().ordinal()+1) >= (destCell.getLevel().ordinal()))
                return true;
            else
                return false;
        }catch (NullPointerException e){
            throw new NullPointerException();
        }
        // how to control if the next level is the dome?
        // possibility of exception?

    }

}
