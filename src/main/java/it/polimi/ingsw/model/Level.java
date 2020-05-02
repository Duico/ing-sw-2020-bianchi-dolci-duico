package it.polimi.ingsw.model;

public enum Level {

    EMPTY(0),
    BASE(1),
    MID(2),
    TOP(3);

    Level(int i){
        ord=i;
    }

    private int ord;

    public int getOrd(){
        return this.ord;
    }



}
