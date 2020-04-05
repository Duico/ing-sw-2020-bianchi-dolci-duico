package it.polimi.ingsw.model;

import java.util.Objects;

public class Position implements Cloneable{
    private int x;
    private int y;
    static int width;
    static int height;

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


    public static final void setSize(int width, int height){
        Position.width = width;
        Position.height = height;

    }

    public boolean equals(Object object) {
        if (!(object instanceof Position))
            return false;
        Position position= (Position) object;
        return (this.x==position.x && this.y == position.y);
    }
}
