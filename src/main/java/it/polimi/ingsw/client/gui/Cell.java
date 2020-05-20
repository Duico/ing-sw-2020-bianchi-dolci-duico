package it.polimi.ingsw.client.gui;
import javafx.scene.Node;

public class Cell {
    private boolean isWorkerSet;
    private Coordinate coordinate;
    private Node worker;
    private Node lastBuilding;
    private boolean isDome;

    Cell(){
        this.isWorkerSet=false;
        this.isDome=false;
        this.worker=null;
        this.lastBuilding=null;
    }

    public void setWorker(Node worker){
        this.worker=worker;
        putWorker();
    }

    public void setLastBuilding(Node node){
        this.lastBuilding=node;
    }

    public Node getLastBuilding(){
        return this.lastBuilding;

    }
    public Node getWorker(){
        return this.worker;
    }

    public void setDome(){
        this.isDome=true;
    }

    public boolean isDome(){
        return this.isDome;
    }

    public void setCoordinate(Coordinate coord){
        this.coordinate=coord;
    }

    public Coordinate getCoordinate(){
        return this.coordinate;
    }

    public void setCoordinateZ(double dz){
        this.coordinate.setCenterZ(dz);
    }

    public boolean isWorkerSet(){
        return isWorkerSet;
    }

    private void putWorker(){
        this.isWorkerSet=true;
    }

    public void deleteWorker(){
        this.isWorkerSet=false;
    }
}
