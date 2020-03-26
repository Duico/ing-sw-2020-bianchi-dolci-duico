package it.polimi.ingsw.model;

public class BoardCell {
    private Level level;
    private boolean hasDome;
    private Position position;
    private Worker worker;

    public BoardCell(Position position){
        this.level = Level.EMPTY;
        this.hasDome = false;
        this.position = position;
        this.worker = null;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public boolean isHasDome() {
        return hasDome;
    }

    public void setHasDome(boolean hasDome) {
        this.hasDome = hasDome;
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
