package it.polimi.ingsw.model;

public interface BuildStrategy {
    boolean isValidBuild(BoardCell cell);
    boolean isAllowedToBuildDome(BoardCell cell);
    boolean isAllowedToBuild();
    boolean isRequiredToBuild();
}
