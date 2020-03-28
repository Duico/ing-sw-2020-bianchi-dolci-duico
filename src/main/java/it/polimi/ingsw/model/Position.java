package it.polimi.ingsw.model;

public class Position {
    private int x;
    private int y;
    static int width = 5;
    static int height = 5;

    public Position(int x, int y) throws PositionOutOfBoundsException {
        if( x < 0 || y < 0 || x >= width || y >= height ) throw new PositionOutOfBoundsException();
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
