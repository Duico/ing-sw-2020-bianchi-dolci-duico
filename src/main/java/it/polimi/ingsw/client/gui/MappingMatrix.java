package it.polimi.ingsw.client.gui;

public class MappingMatrix {
    private Border map[][]= new Border[5][5];

    MappingMatrix(double size) {
        double gap = size / 5;
        double x = -(size / 2);
        double y = -(size / 2);
        double copyX = x;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                double left=x;
                x+=gap;
                map[i][j]= new Border(y,y+gap,left,x);
            }
            x=copyX;
            y=y+gap;
        }

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
}
