package it.polimi.ingsw.client.gui;
import javafx.scene.Node;

public class GuiCell {
    private Coordinate coordinate;
    private Node worker;
    private Node lastBuilding;
    private boolean isDome;
    private Dimension level;

    GuiCell(){
        this.level=Dimension.EMPTY;
        this.isDome=false;
        this.worker=null;
        this.lastBuilding=null;
    }

    public void setLevel(Dimension level){
        this.level=level;
        setCoordinateZ(level.getHeight());
    }

    public Dimension getLevel(){
        return this.level;
    }

    public void setWorker(Node worker){
        this.worker=worker;
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

    private void setCoordinateZ(double dz){
        this.coordinate.setCenterZ(dz);
    }

    public boolean isWorkerSet(){
        return this.worker!=null;
    }

    public void deleteWorker(){
        this.worker=null;
    }
}
