package it.polimi.ingsw.model;

public class DownMoveWin implements WinStrategy {
    @Override
    public boolean isWinningMove(BoardCell[][] grid, Position startPosition, Position destinationPosition){
        try{
            BoardCell startCell = grid[destinationPosition.getX()][destinationPosition.getY()];
            BoardCell destinationCell = grid[destinationPosition.getX()][destinationPosition.getY()];
            if(startCell.getLevel().ordinal() == 2 && destinationCell.getLevel().ordinal() == 0 && !destinationCell.hasDome() && destinationCell.getWorker() == null)
                return true;
            else
                return false;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }


}
