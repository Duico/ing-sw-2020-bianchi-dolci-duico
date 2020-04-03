package it.polimi.ingsw.model;

import java.util.ArrayList;

public class DefaultBuild implements BuildStrategy {

    /**
     * Class that implements the default strategy of the BuildStrategy
     */
    @Override
    public boolean isValidBuild(BoardCell [][] grid, Position startPosition, Position destinationPosition, Boolean isDome, ArrayList<Position> builds) {
        BuildStrategy domeBuild = new DomeBuild();
        Boolean isValidBuild = domeBuild.isValidBuild(grid, startPosition, destinationPosition,isDome, builds);
        Boolean rejectedDome = isDome && !this.isAllowedToBuildDome(grid, destinationPosition);
        if(isValidBuild && !rejectedDome)
            return true;
        else
            return false;

    }


}
