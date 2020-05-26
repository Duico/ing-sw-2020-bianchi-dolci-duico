package it.polimi.ingsw.client.gui;

public class CoordinateMap {
    private Border map[][]= new Border[5][5];
    private Coordinate [][] coordinateMap= new Coordinate[5][5];
    private final double coordinateZ=0;

    CoordinateMap(double size) {
        double gap = size / 5;
        double x = -(size / 2);
        double y = -(size / 2);
        double copyX = x;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                double left=x;
                x+=gap;
                map[i][j]= new Border(y,y+gap,left,x);
                coordinateMap[i][j]= getCenter(new Border(y,y+gap,left,x));
            }
            x=copyX;
            y=y+gap;
        }

    }

    private Coordinate getCenter(Border border){
        double centerX,centerY;
        double diffX= (border.getRight() - border.getLeft())/2;
        double diffY= (border.getDown() - border.getTop())/2;
        centerX = border.getLeft()+diffX;
        centerY = border.getTop()+diffY;

        Coordinate center = new Coordinate(centerX,centerY, coordinateZ);
        return center;
    }




    public double getTop(int i,int j){
        return map[i][j].getTop();
    }

    public double getDown(int i,int j){
        return map[i][j].getDown();
    }

    public double getLeft(int i,int j){
        return map[i][j].getLeft();
    }

    public double getRight(int i,int j){
        return map[i][j].getRight();
    }

    public Border getBorder(int i, int j){
        return map[i][j];
    }

    public Coordinate getCoordinate(int i, int j){
        return coordinateMap[i][j];
    }
}
