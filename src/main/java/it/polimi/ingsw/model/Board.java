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


    public boolean validMove(Position startPosition, Position destinationPosition){
        BoardCell startCell = getBoardCell(startPosition);
        BoardCell destinationCell = getBoardCell(destinationPosition);
        BlockStrategy blockStrategy = startCell.getWorker().getCard().getBlockStrategy();
        MoveStrategy moveStrategy = startCell.getWorker().getCard().getMoveStrategy();
        OpponentStrategy opponentStrategy = startCell.getWorker().getCard().getOpponentStrategy();


        boolean isValidMove = moveStrategy.isValidMove(startCell, destinationCell);
        boolean isValidPush = opponentStrategy.isValidPush(this, destinationCell);

        if( !blockStrategy.isValidMoveForNextPlayer(this, cell)) throw new BlockedMoveException();
        else if( !moveStrategy.isAllowedToMove(this) ) throw new NotAllowedMoveException();
        else if ( !isValidMove || !isValidPush ) throw new NotValidMoveException();
        else {
            opponentStrategy.pushOpponent(this, cell);
            this.updatePosition(cell);
        }
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
    public Worker playerOfTheCell(){

    }
}
