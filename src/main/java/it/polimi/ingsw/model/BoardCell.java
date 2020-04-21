package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

public class BoardCell implements Cloneable, Serializable {
    private Level level;
    private boolean dome;
    private Worker worker;

    public BoardCell(){
        this.level = Level.EMPTY;
        this.dome = false;
        this.worker = null;
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
            boardCell.worker = ( this.worker!=null )? this.worker.clone():null;
        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported on BoardCell");
        }
        return boardCell;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardCell boardCell = (BoardCell) o;
        boolean equalWorkers = (getWorker() == null && boardCell.getWorker() == null) || getWorker().equals(boardCell.getWorker());
        return equalWorkers && getLevel().equals(boardCell.getLevel()) && hasDome() == boardCell.hasDome();
    }

}
