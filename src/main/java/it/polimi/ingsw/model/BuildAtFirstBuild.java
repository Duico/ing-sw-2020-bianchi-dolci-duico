package it.polimi.ingsw.model;



public class BuildAtFirstBuild implements BuildStrategy {

    @Override
    public boolean isValidBuild(Worker worker, BoardCell cell) {
        return false;
    }

    @Override
    public boolean isAllowedToBuildDome(Worker worker, BoardCell cell) {
        return false;
    }

    @Override
    public boolean isAllowedToBuild(Worker worker) {
        return false;
    }

    @Override
    public boolean isRequiredToBuild(Worker worker) {
        return false;
    }
}
