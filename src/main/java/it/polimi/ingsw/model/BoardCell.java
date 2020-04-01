package it.polimi.ingsw.model;

public class BoardCell implements Cloneable{
    private Level level;
    private boolean dome;
    private Position position;
    private Worker worker;

    public BoardCell(Position position){
        this.level = Level.EMPTY;
        this.dome = false;
        this.position = position;
        this.worker = null;
    }
    private void updateLevel(Level level, boolean hasDome){
        this.setLevel(level);
        this.setDome(hasDome);
    }

    public Object clone() throws
            CloneNotSupportedException
    {
        return super.clone();
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setDome(boolean hasDome) {
        this.dome = hasDome;
    }

    public boolean hasDome() {
        return dome;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
