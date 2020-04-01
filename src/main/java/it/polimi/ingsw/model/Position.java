package it.polimi.ingsw.model;

public class Position {
    private int x;
    private int y;
    private int width;  // decide how to set: Configuration file of the game and set this with static attribute?
    private int height;

    public Position(int width, int height, int x, int y) throws PositionOutOfBoundsException {
        this.width = width;
        this.height = height;
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
