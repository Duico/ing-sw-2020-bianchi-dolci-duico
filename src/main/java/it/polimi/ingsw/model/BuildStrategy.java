package it.polimi.ingsw.model;

public interface BuildStrategy {
    boolean isValidBuild(Worker worker, BoardCell cell);
    boolean isAllowedToBuildDome(Worker worker, BoardCell cell);
    boolean isAllowedToBuild(Worker worker);
    boolean isRequiredToBuild(Worker worker);
}
