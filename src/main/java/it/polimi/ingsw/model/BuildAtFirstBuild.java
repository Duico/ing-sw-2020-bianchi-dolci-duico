package it.polimi.ingsw.model;

public class BuildAtFirstBuild implements BuildStrategy {
    public boolean isValidBuild(BoardCell cell) {
        return false;
    }

    public boolean isAllowedToBuildDome(BoardCell cell) {
        return false;
    }

    public boolean isAllowedToBuild() {
        return false;
    }

    public boolean isRequiredToBuild() {
        return false;
    }
}
