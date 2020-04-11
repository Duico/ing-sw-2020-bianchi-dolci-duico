package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.*;
import it.polimi.ingsw.model.strategy.*;

import java.io.Serializable;
import java.util.ArrayList;

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


    public boolean canMove(Position startPosition, Position destinationPosition, boolean isPreviousBlockMove, boolean isOwnWorker, Card card, Card previousCard) throws BlockedMoveException {

        if(isPreviousBlockMove == true) {
            boolean isBlockedMove = this.isBlockedMove(startPosition, destinationPosition, previousCard.getBlockStrategy());
            if (isBlockedMove) {
                return false;
            }
        }
        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, this.grid);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, isOwnWorker, this.grid);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }

     private boolean isBlockedMove(Position startPosition, Position destinationPosition, BlockStrategy blockStrategy){
        return blockStrategy.isBlockMove( startPosition, destinationPosition, this.grid);
    }

    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, Card card) {
        boolean willBlock = card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, this.grid);
        return willBlock;
    }

    public boolean canBuild(Position startPosition, Position destinationPosition, Card card, boolean isDome){
        boolean isValidBuild = card.getBuildStrategy().isValidBuild(this.grid, startPosition, destinationPosition,isDome);
        return isValidBuild;

    }

    public void build(Position startPosition, Position destinationPosition, boolean isDome) {
        if(isDome){
            this.grid[destinationPosition.getX()][destinationPosition.getY()].setDome(true);
            // aggiungo lo stesso addBuild?
        }else{
            int previousLevel = this.grid[destinationPosition.getX()][destinationPosition.getY()].getLevel().ordinal();
            this.grid[destinationPosition.getX()][destinationPosition.getY()].setLevel(Level.values()[previousLevel+1]);
            this.grid[startPosition.getX()][destinationPosition.getY()].getWorker().addBuild(destinationPosition);
        }
    }



    private BoardCell getBoardCell(Position position) throws CloneNotSupportedException {
        BoardCell cell = (BoardCell) grid[position.getX()][position.getY()].clone();
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

    public void setWorker(Worker worker, Position destPosition){
        if( this.getBoardCellReference(destPosition).getWorker() == null){
            this.getBoardCellReference(destPosition).setWorker(worker);
            this.getBoardCellReference(destPosition).getWorker().addMove(destPosition);
        }
    }

    public boolean isWinningMove(Position startPosition, Position destinationPosition, Card card){
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, this.grid );
    }

    public boolean isLoseCondition(ArrayList<Position> currentPositions, boolean isPreviousBlockMove, Card card, Card previousCard) {
        boolean loseCondition = true;
        for (int i=0; i<currentPositions.size(); i++){
            int currentY = currentPositions.get(i).getY();
            for(int y= -1; y<=1; y++) {
                int positionY=currentY+y;
                if (positionY >= 0 && positionY < Position.height) {
                    int currentX = currentPositions.get(i).getX();
                    for (int x = -1; x <=1; x++) {
                        int positionX=currentX+x;
                        if (positionX >= 0 && positionX < Position.width) {
                            if(positionX!=currentPositions.get(i).getX() || positionY!=currentPositions.get(i).getY()) {
                                try {
                                    Position startPosition = currentPositions.get(i);
                                    Position destPostion = new Position(positionX, positionY);
                                    boolean isOwnWorker=false;
                                    for(Position position: currentPositions){
                                        if(position.equals(destPostion))
                                            isOwnWorker=true;
                                    }
                                    try {
                                        boolean canMove = this.canMove(startPosition, destPostion, isPreviousBlockMove, isOwnWorker, card, previousCard);
                                        if (canMove == true)
                                            return false;
                                    }catch (BlockedMoveException e){
                                        return false;
                                    }
                                }catch(PositionOutOfBoundsException e){
                                    continue;
                                }

                            }
                        }
                    }
                }
            }
        }
        return loseCondition;
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
