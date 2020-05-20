package it.polimi.ingsw.client.gui;

public class Coordinate {
    private double centerX;
    private double centerY;
    private double centerZ;

    Coordinate(double x, double y){
        this.centerX=x;
        this.centerY=y;
    }

    Coordinate(double x, double y, double z){
        this.centerX=x;
        this.centerY=y;
        this.centerZ=z;
    }

    public void setCenterZ(double dz){
        this.centerZ=dz;
    }
    public double getCenterZ(){ return centerZ; }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }
}
