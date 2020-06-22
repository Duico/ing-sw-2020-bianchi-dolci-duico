package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Represent the  Board of the game where player can place, move or build with their workers
 */
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

    /**
     * increment level in the destination position BoardCell it is not a dome build
     * if it is a dome build sets the boolean attribute that specifies if a dome is built in the destination position BoardCell
     * @param startPosition position where the worker is located
     * @param destinationPosition build position
     * @param isDome specifies whether or not the player is building a dome
     */
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

    /**
     * return the BoardCell corresponding to the Position got from parameter
     * @param position used to get related BoardCell reference
     * @return
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    private BoardCell getBoardCellReference(Position position) throws NullPointerException,IndexOutOfBoundsException{
        try{
            return grid[position.getX()][position.getY()];
        }catch(NullPointerException |IndexOutOfBoundsException e){
            throw e;
        }
    }

    /**
     * removes the worker situated in the start position BoardCell and puts it in the
     * destination position BoardCell
     * updates the List containing the current worker's moves
     * @param startPosition position where the worker is located
     * @param destinationPosition position where the worker is moving
     * @param pushDestPosition position where the worker located in destination position BoardCell is moving
     */
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

    /**
     * sets the worker got from parameter to the destination position BoardCell
     * @param worker worker to set
     * @param destPosition position where the worker is set
     * @return
     */
    public boolean setWorker(Worker worker, Position destPosition){
        if( this.getBoardCellReference(destPosition).getWorker() == null){
            this.getBoardCellReference(destPosition).setWorker(worker);
            this.getBoardCellReference(destPosition).getWorker().addMove(destPosition);
            return true;
        }else{
            return false;
        }
    }

    /**
     * deletes Worker reference from the BoardCell corresponding to the position got from parameter
     * @param position worker position
     * @return
     */
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
