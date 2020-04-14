package it.polimi.ingsw.model;

import java.io.Serializable;

public class Board implements Cloneable, Serializable {
    BoardCell[][] grid;

    public Board() { //decide if throws exception or not
        int width = Position.width;
        int height = Position.height;
        grid = new BoardCell[width][height];
        for(int i=0;i<height;i++){
            for(int j=0; j<width; j++){
                grid[i][j]= new BoardCell();
            }
        }
    }

    public void build(Position startPosition, Position destinationPosition, boolean isDome) {
        Worker worker = this.grid[startPosition.getX()][startPosition.getY()].getWorker();
        if(worker == null){
            throw new NullPointerException("Worker not set inside BoardCell in startPosition");
        }
        if(isDome){
            this.grid[destinationPosition.getX()][destinationPosition.getY()].setDome(true);
        }else{
            int previousLevel = this.grid[destinationPosition.getX()][destinationPosition.getY()].getLevel().ordinal();
            this.grid[destinationPosition.getX()][destinationPosition.getY()].setLevel(Level.values()[previousLevel+1]);
        }
        worker.addBuild(destinationPosition);
    }

    public BoardCell getBoardCell(Position position) {
            BoardCell cell = grid[position.getX()][position.getY()].clone();
            return cell;
    }

    private BoardCell getBoardCellReference(Position position){
        return grid[position.getX()][position.getY()];
    }


    public void putWorkers(Position startPosition, Position destinationPosition, Position pushDestPosition){
        Worker temp = this.getBoardCellReference(startPosition).getWorker();
        this.getBoardCellReference(destinationPosition).setWorker(this.getBoardCellReference(startPosition).getWorker());
        if(pushDestPosition != null){
            this.getBoardCellReference(pushDestPosition).getWorker().addMove(pushDestPosition);
            this.getBoardCellReference(destinationPosition).setWorker(temp);
        }
        this.getBoardCellReference(startPosition).setWorker(null);
        this.getBoardCellReference(destinationPosition).getWorker().addMove(destinationPosition);
    }

    public boolean setWorker(Worker worker, Position destPosition){
        if( this.getBoardCellReference(destPosition).getWorker() == null){
            this.getBoardCellReference(destPosition).setWorker(worker);
            this.getBoardCellReference(destPosition).getWorker().addMove(destPosition);
            return true;
        }else{
            return false;
        }
    }

    @Override
    protected Board clone() {
        int width = Position.width;
        int height = Position.height;
        Board board = null;
        BoardCell[][] newGrid = new BoardCell[width][height];

        //DEEP cloning for each BoardCell in grid
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                newGrid[i][j] = this.grid[i][j].clone();
            }
        }
        try {
            board = (Board) super.clone();
            board.grid = newGrid;
        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported in Board");
        }
        return board;
    }

}
