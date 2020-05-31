package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Position;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;

public class CoordinateMap {
    private Bounds[][] map2D = new BoundingBox[5][5];
    private double[][] heights = new double[5][5];
//    private Node[][] lastBuildings = new Node[5][5];
    private final double baseZ;
//    private Coordinate [][] coordinateMap= new Coordinate[5][5];

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
                //                coordinateMap[i][j]= getCenter(new Border(y,y+gap,left,x));
            }

            x=copyX;
            y=y+gap;
        }

    }

//    private Point3D getCenter(Bounds bounds){
//        double centerX= bounds.getCenterX();
//        double centerY= bounds.getCenterY();
//        double topZ = bounds.getMaxZ();
//
//        Point3D center = new Point3D(centerX,centerY,topZ);
//        return center;
//    }

//    public double getTop(int i,int j){
//        return map2D[i][j].getMaxY();
//    }
//
//    public double getDown(int i,int j){
//        return map2D[i][j].getMinY();
//    }
//
//    public double getLeft(int i,int j){
//        return map[i][j].getMinX();
//    }
//
//    public double getRight(int i,int j){
//        return map[i][j].getMaxX();
//    }

    public Bounds getBoundingBox(int i, int j){
        return map2D[i][j];
    }

    public Bounds getBoundingBox(Position position){
        return getBoundingBox(position.getX(), position.getY());
    }

    private Point3D getCoordinate(int i, int j){
        Bounds bb2D = getBoundingBox(i,j);
        return new Point3D(bb2D.getCenterX(), bb2D.getCenterY(), heights[i][j]);
    }

    /**
     *
     * @param position
     * @return Center of the bounding box fox X,Y. Top of the Box for Z
     */
    public Point3D getCoordinate(Position position){
        return getCoordinate(position.getX(), position.getY());
    }

//    /**
//     * Needed for making the last building block mouse-transparent
//     * @param position
//     * @return
//     */
//    public Node getLastBuilding(Position position){
//        try{
//            return lastBuildings[position.getX()][position.getY()];
//        }catch(IndexOutOfBoundsException e){
//            return null;
//        }
//    }

    public boolean setHeight(Position position, BuildingHeight level){
        try {
//            lastBuildings[position.getX()][position.getY()] = lastNode;
//            Bounds old = map2D[position.getX()][position.getY()];
            heights[position.getX()][position.getY()] = level.getHeight();
            return true;
        }catch(IndexOutOfBoundsException e){
            return false;
        }
    }

    public double getHeight(int i, int j){
        return heights[i][j];
    }
}
