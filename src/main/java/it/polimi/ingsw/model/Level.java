package it.polimi.ingsw.model;

import it.polimi.ingsw.client.gui.BuildingHeight;

/**
 * Represent building levels related to a single BoardCell, used for movements and buildings
 */
public enum Level {

    EMPTY(0),
    BASE(1),
    MID(2),
    TOP(3);

    Level(int i){
        ord=i;
    }

    private final int ord;

    public int getOrd(){
        return this.ord;
    }

    /**
     * Return a level from an int
     * @param i
     * @return level BoardCell level
     */
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

    /**
     * used by Gui controller to set exact Z coordinate in GuiModel
     * @param level BoardCell level
     * @return
     */
    public static BuildingHeight fromLevelToBuildingHeight(Level level){
        if(level.equals(Level.EMPTY))
            return BuildingHeight.EMPTY;
        else if(level.equals(Level.BASE))
            return BuildingHeight.BASE;
        else if(level.equals(Level.MID))
            return BuildingHeight.MID;
        else if(level.equals(Level.TOP))
            return BuildingHeight.TOP;
        else
            throw new IllegalArgumentException();
    }
}
