package it.polimi.ingsw.client.gui;

public class Border {
    private double top;
    private double down;
    private double left;
    private double right;

    Border(double top, double down, double left,double right){
        this.top=top;
        this.down=down;
        this.left=left;
        this.right=right;
    }

    public double getTop() {
        return top;
    }

    public double getDown() {
        return down;
    }

    public double getLeft(){
        return left;
    }

    public double getRight(){
        return right;
    }
}
