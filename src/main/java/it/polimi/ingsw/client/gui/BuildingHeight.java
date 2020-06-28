package it.polimi.ingsw.client.gui;

/**
 * Represent Z coordinate reached by buildings related to every level
 */
public enum BuildingHeight {
    EMPTY(0),
    BASE(-1.153),
    MID(-2.264),
    TOP(-2.864);
    private final double height;
    private BuildingHeight(double height){
        this.height = height;
    }
    public double getHeight() {
        return height;
    }
}
