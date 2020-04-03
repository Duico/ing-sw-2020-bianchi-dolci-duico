package it.polimi.ingsw.model;

import java.util.ArrayList;

public class DomeBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds) {
        try {
            BoardCell startCell = grid[startPosition.getX()][startPosition.getY()];
            BoardCell destinationCell = grid[destinationPosition.getX()][destinationPosition.getY()];
            int dx = startPosition.getX() - destinationPosition.getX();
            int dy = startPosition.getX() - destinationPosition.getY();
            if (dx == 0 && dy == 0)
                return false;
            else if (dx < -1 || dx > 1 || dy < -1 || dy > 1)
                return false;
            else if (destinationCell.hasDome())
                return false;
            else if (destinationCell.getWorker() != null)
                return false;
            else
                return true;
        }
        catch (NullPointerException e){
            throw new NullPointerException();
        }
    }

    @Override
    public boolean isAllowedToBuildDome(BoardCell [][] grid, Position buildPosition)
    {
        return true;
    }


}
