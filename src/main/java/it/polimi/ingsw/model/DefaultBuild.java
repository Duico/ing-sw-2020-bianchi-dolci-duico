package it.polimi.ingsw.model;

public class DefaultBuild implements BuildStrategy {
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
