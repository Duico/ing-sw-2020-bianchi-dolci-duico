package it.polimi.ingsw.model;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class Board implements Cloneable{
    BoardCell[][] grid;

    public Board() throws PositionOutOfBoundsException { //decide if throws exception or not
        int width = Position.width;
        int height = Position.height;
        grid = new BoardCell[width][height];
        for(int i=0;i<height;i++){
            for(int j=0; j<width; j++){
                Position initialPosition = new Position(i, j);
                grid[i][j]= new BoardCell(initialPosition);
            }
        }
    }

    public boolean canMove(Position startPosition, Position destinationPosition, ArrayList<Position> previousMoves,Card card, Card previousCard) throws BlockedMoveException {

        boolean isNotBlocked = this.isNotBlocked(startPosition, destinationPosition, previousMoves, previousCard.getBlockStrategy() );

        if(isNotBlocked){
            throw new BlockedMoveException();
            return false;
        }

        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, this.grid);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, this.grid);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }

    /**
     *
     * @param startPosition
     * @param destinationPosition
     * @param card
     * @return True if blocking effect is active
     */
    private boolean isNotBlocked(Position startPosition, Position destinationPosition, ArrayList<Position> previousMoves, BlockStrategy blockStrategy){
        return blockStrategy.isValidMoveForNextPlayer(startPosition, destinationPosition, previousMoves, this.grid);
    }

    public boolean canBuild(Position startPosition, Position destinationPosition, Card card){
        return true;
    }

    private BoardCell getBoardCell(Position position) throws CloneNotSupportedException {
        BoardCell cell = (BoardCell) grid[position.getX()][position.getY()].clone();
        return cell;
    }

    private BoardCell getBoardCellReference(Position position){
        return grid[position.getX()][position.getY()];
    }

    public void setWorkerInTheCell(Position startPosition, Position destinationPosition){
        this.getBoardCellReference(destinationPosition).setWorker(this.getBoardCellReference(startPosition).getWorker());
        this.getBoardCellReference(startPosition).setWorker(null);
    }

    public void pushWorkersInTheCells(Position startPosition, Position destinationPosition, Position pushDestPosition){
        Worker temp = this.getBoardCellReference(startPosition).getWorker();
        this.getBoardCellReference(pushDestPosition).setWorker(this.getBoardCellReference(startPosition).getWorker());
        this.getBoardCellReference(pushDestPosition).getWorker().addMove(destinationPosition);
        //this.getBoardCellReference(pushDestPosition).getWorker().setCurrentPosition(pushDestPosition);
        this.getBoardCellReference(destinationPosition).setWorker(temp);
        this.getBoardCellReference(destinationPosition).getWorker().addMove(startPosition);
        //this.getBoardCellReference(pushDestPosition).getWorker().setCurrentPosition(startPosition);

    }

    @Override
    protected Board clone() throws CloneNotSupportedException {
        int width = Position.width;
        int height = Position.height;

        BoardCell[][] newGrid = new BoardCell[width][height];

        //DEEP cloning for each BoardCell in grid
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                newGrid[i][j] = (BoardCell) this.grid[i][j].clone();
            }
        }
        Board board = (Board) super.clone();
        board.grid = newGrid;
        return board;
    }
}
