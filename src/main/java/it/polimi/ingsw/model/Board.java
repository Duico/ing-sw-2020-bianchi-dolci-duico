package it.polimi.ingsw.model;


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

    public void setWorker(Position destPosition, Worker worker){
        if( this.getBoardCellReference(destPosition).getWorker() == null){
            this.getBoardCellReference(destPosition).setWorker(worker);
            this.getBoardCellReference(destPosition).getWorker().addMove(destPosition);
        }
    }

    public boolean isWinningMove(Position startPosition, Position destinationPosition, Card card){
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, this.grid );
    }

    public boolean isLoseCondition(ArrayList<Position> currentPositions) {
        boolean loseCondition = true;
        for (int i=0; i<currentPositions.size(); i++){
            int currentY = currentPositions.get(i).getY()-1;
            for(int y=0; y<3; y++) {
                if ((currentY + y) >= 0 && (currentY + y) < Position.height) {
                    int currentX = currentPositions.get(i).getX() - 1;
                    for (int x = 0; x < 3; x++) {
                        if ((currentX + x) >= 0 && (currentX + x) < Position.width) {
                            int positionX=currentX+x;
                            int positionY=currentY+y;
                            if(positionX!=currentPositions.get(i).getX() || positionY!=currentPositions.get(i).getY()) {
                                int startLevel = grid[currentPositions.get(i).getX()][currentPositions.get(i).getY()].getLevel().ordinal();
                                int aroundLevel = grid[positionX][positionY].getLevel().ordinal();
                                boolean hasDome = grid[positionX][positionY].hasDome();
                                if (startLevel >= aroundLevel && !hasDome) {  //se può scendere perchè around level più basso e non ha dome
                                    loseCondition = false;
                                    break;
                                } else if (aroundLevel == (startLevel + 1) && !hasDome) {  // se può salire perchè around level più alto di uno e non ha dome
                                    loseCondition = false;
                                    break;
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
