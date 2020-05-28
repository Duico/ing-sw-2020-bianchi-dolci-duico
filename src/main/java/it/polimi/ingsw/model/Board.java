package it.polimi.ingsw.model;

import java.io.Serializable;

public class Board implements Cloneable, Serializable {
    private BoardCell[][] grid;
    private final int width;
    private final int height;

    public Board() { //decide if throws exception or not
        this(Position.width, Position.height);
    }
    public Board(int width, int height){
        this.width = width;
        this.height = height;
        grid = new BoardCell[width][height];
        for(int i=0;i<height;i++){
            for(int j=0; j<width; j++){
                grid[i][j]= new BoardCell();
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void build(Position startPosition, Position destinationPosition, boolean isDome) {
        Worker worker = this.getBoardCellReference(startPosition).getWorker();
        if(worker == null){
            throw new NullPointerException("Worker not set inside BoardCell in startPosition");
        }
        if(isDome){
            getBoardCellReference(destinationPosition).setDome(true);
        }else{
            int previousLevel = getBoardCellReference(destinationPosition).getLevel().getOrd();
            getBoardCellReference(destinationPosition).setLevel(Level.values()[previousLevel+1]);

        }
        worker.addBuild(destinationPosition);
    }

    public BoardCell getBoardCell(Position position) {
            BoardCell cell = getBoardCellReference(position).clone();
            return cell;
    }

    private BoardCell getBoardCellReference(Position position) throws NullPointerException,IndexOutOfBoundsException{
        try{
            return grid[position.getX()][position.getY()];
        }catch(NullPointerException |IndexOutOfBoundsException e){
            throw e;
        }
    }


    public void putWorkers(Position startPosition, Position destinationPosition, Position pushDestPosition){
        BoardCell startBoardCell = getBoardCellReference(startPosition);
        Worker startWorker = startBoardCell.getWorker();
        if(startWorker == null){
            throw new NullPointerException("Worker not set inside BoardCell in startPosition");
        }
        BoardCell destinationCell = getBoardCellReference(destinationPosition);
        startBoardCell.setWorker(null);
        if(pushDestPosition != null) {
            Worker destinationWorker = destinationCell.getWorker();
            if(destinationWorker != null){
                BoardCell pushDestBoardCell = getBoardCellReference(pushDestPosition);
                pushDestBoardCell.setWorker(destinationWorker);
                destinationWorker.addMove(pushDestPosition);
            }
        }
        destinationCell.setWorker(startWorker);
        startWorker.addMove(destinationPosition);
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

    public boolean removeWorker(Position position){
        if( this.getBoardCellReference(position).getWorker() != null ){
            this.getBoardCellReference(position).setWorker(null);
            return true;
        }
        return false;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        boolean eq = true;
        for(int i=0;i<Position.width;i++){
            for(int j=0; j<Position.height; j++){
                eq = eq && grid[i][j].equals(board.grid[i][j]);
            }
        }
        return eq;
    }
}
