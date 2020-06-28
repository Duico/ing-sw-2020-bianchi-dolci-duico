package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Position;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;

/**
 * class used to map coordinates on GUI 3D board for heights of buildings and ground level
 */
public class CoordinateMap {
    private Bounds[][] map2D = new BoundingBox[5][5];
    private double[][] heightMap = new double[5][5];
//    private Node[][] lastBuildings = new Node[5][5];
    private final double baseZ;
//    private Coordinate [][] coordinateMap= new Coordinate[5][5];

    /**
     * calculates and inserts into map every BoardCell's boundings coordinates
     * @param size height and width of board
     * @param baseZ Z coordinate of ground level on board
     */
    CoordinateMap(double size, double baseZ) {

        this.baseZ = baseZ;
        double gap = size / 5;
        double x = -(size / 2);
        double y = -(size / 2);
        double copyX = x;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                double left=x;
                x+=gap;
                map2D[j][i] = new BoundingBox(left, y, gap, gap);

            }

            x=copyX;
            y=y+gap;


        }

    }


    public Bounds getBoundingBox(int i, int j){
        return map2D[i][j];
    }

    public Bounds getBoundingBox(Position position){
        return getBoundingBox(position.getX(), position.getY());
    }

    private Point3D getCoordinate(int i, int j){
        Bounds bb2D = getBoundingBox(i,j);
        return new Point3D(bb2D.getCenterX(), bb2D.getCenterY(), heightMap[i][j]);
    }

    /**
     *
     * @param position
     * @return Center of the bounding box fox X,Y. Top of the Box for Z
     */
    public Point3D getCoordinate(Position position){
        return getCoordinate(position.getX(), position.getY());
    }

    /**
     * sets height in a position based on building height already reached
     * @param position position to set height on
     * @param level building level on position
     * @return true if height is successfully set on map
     */
    public boolean setHeight(Position position, BuildingHeight level){
        try {
            heightMap[position.getX()][position.getY()] = level.getHeight();
            return true;
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public double getHeight(int i, int j){
        return heightMap[i][j];
    }

    /**
     * clears map containing heights on board
     */
    public void clearHeight(){
        for(int i=0;i<5;i++){
            for(int j=0;j<5;j++){
                heightMap[i][j] = 0;
            }
        }
    }
}
