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

    public static Level fromIntToLevel(int i){
        if(i==0)
            return Level.EMPTY;
        else if(i==1)
            return Level.BASE;
        else if(i==2)
            return Level.MID;
        else if(i==3)
            return Level.TOP;
        else
            return null;
    }

}
