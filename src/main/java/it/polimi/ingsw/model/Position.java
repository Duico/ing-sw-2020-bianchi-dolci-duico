package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.io.Serializable;
import java.util.Objects;

/**
 * defines size of the Board and range of possible positions accepted
 * every BoardCell is related to a Position
 */
public class Position implements Serializable, Cloneable{
    private final int x;
    private final int y;
    static int width = 5;
    static int height = 5;

    /**
     *
     * @param x coordinate x of the position
     * @param y coordinate y of the position
     * @throws PositionOutOfBoundsException
     */
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


    public static void setSize(int width, int height){
        Position.width = width;
        Position.height = height;

    }



    public boolean equals(Object object) {
        if (!(object instanceof Position))
            return false;
        Position position= (Position) object;
        return (this.x==position.x && this.y == position.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
