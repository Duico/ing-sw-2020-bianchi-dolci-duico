package it.polimi.ingsw.model;

public class DownMoveWin implements WinStrategy {
    @Override
    public boolean isWinningMove(Position startPosition, Position destinationPosition, BoardCell[][] grid){
        try{
            BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
            BoardCell destinationCell = grid[destinationPosition.getX()][destinationPosition.getY()];
            if(startCell.getLevel().ordinal() == 2 && destinationCell.getLevel().ordinal() == 0)
                return true;
            if(startCell.getLevel().ordinal() == 3 && (destinationCell.getLevel().ordinal() == 0 || destinationCell.getLevel().ordinal() == 1))
                return true;
            else
                return false;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }


}
