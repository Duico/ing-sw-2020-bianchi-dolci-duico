package it.polimi.ingsw.model;

public class BoardCell implements Cloneable{
    private Level level;
    private boolean dome;
    private Worker worker;

    public BoardCell(){
        this.level = Level.EMPTY;
        this.dome = false;
        this.worker = null;
    }
    private void updateLevel(Level level, boolean hasDome){
        this.setLevel(level);
        this.setDome(hasDome);
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


    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Object clone() throws CloneNotSupportedException {
        BoardCell boardCell = (BoardCell) super.clone();
        boardCell.worker= (Worker) this.worker.clone();
        return boardCell;
    }
}
