package it.polimi.ingsw.model;

import java.io.Serializable;

public class BoardCell implements Cloneable, Serializable {
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

    @Override
    public BoardCell clone() {
        BoardCell boardCell;
        try {
            boardCell = (BoardCell) super.clone();
            boardCell.worker = this.worker.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported on BoardCell");
        }
        return boardCell;
    }
}
