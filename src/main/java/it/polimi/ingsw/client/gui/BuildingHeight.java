package it.polimi.ingsw.client.gui;

public enum BuildingHeight {
    EMPTY(0),
    BASE(-1.323),
    MID(-2.434),
    TOP(-3.034);
    private final double height;
    private BuildingHeight(double height){
        this.height = height;
    }
    public double getHeight() {
        return height;
    }
}
