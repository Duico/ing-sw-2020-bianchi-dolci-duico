package it.polimi.ingsw.model;

public class Board {
    BoardCell[][] grid;

    public Board(int width, int height) throws PositionOutOfBoundsException { //decide if throws exception or not
        grid = new BoardCell[width][height];
        for(int i=0;i<width;i++){
            for(int j=0; j<height; j++){
                Position initialPosition = new Position(i, j);
                grid[i][j]= new BoardCell(initialPosition);
            }
        }
    }

    public boolean canMove(Position startPosition, Position destinationPosition, Card card){
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

    public boolean isBlockMove(Position startPosition, Position destinationPosition, Card card){
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

    public boolean canBuild(Position startPosition, Position destinationPosition, Card card){
        return true;
    }

    public BoardCell getBoardCell(Position position) throws CloneNotSupportedException {
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
        this.getBoardCellReference(pushDestPosition).getWorker().addMoves(destinationPosition);
        this.getBoardCellReference(pushDestPosition).getWorker().setCurrentPosition(pushDestPosition);
        this.getBoardCellReference(destinationPosition).setWorker(temp);
        this.getBoardCellReference(destinationPosition).getWorker().addMoves(startPosition);
        this.getBoardCellReference(pushDestPosition).getWorker().setCurrentPosition(startPosition);
        
    }


}
