package it.polimi.ingsw.model;


public class Board implements Cloneable{
    BoardCell[][] grid;

    public Board() throws PositionOutOfBoundsException { //decide if throws exception or not
        int width = Position.width;
        int height = Position.height;
        grid = new BoardCell[width][height];
        for(int i=0;i<height;i++){
            for(int j=0; j<width; j++){
                grid[i][j]= new BoardCell();
            }
        }
    }


    public boolean canMove(Position startPosition, Position destinationPosition, boolean isPreviousBlockMove, Card card, Card previousCard) throws BlockedMoveException {

        if(isPreviousBlockMove == true) {
            boolean isBlockedMove = this.isBlockedMove(startPosition, destinationPosition, previousCard.getBlockStrategy());
            if (isBlockedMove) {
                return false;
            }
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

    public boolean canBuild(Position startPosition, Position destinationPosition, Card card){
        return true;
    }

    private boolean isBlockedMove(Position startPosition, Position destinationPosition, BlockStrategy blockStrategy){
        return blockStrategy.isBlockMove( startPosition, destinationPosition, this.grid);
    }

    public boolean blockNextPlayer(Position startPosition, Position destinationPosition, Card card) {
        boolean willBlock = card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, this.grid);
        return willBlock;
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
        if(pushDestPosition == null){
            this.getBoardCellReference(startPosition).setWorker(null);
            this.getBoardCellReference(destinationPosition).getWorker().addMove(destinationPosition);
        }else{
            this.getBoardCellReference(pushDestPosition).getWorker().addMove(pushDestPosition);
            this.getBoardCellReference(destinationPosition).setWorker(temp);
            this.getBoardCellReference(startPosition).setWorker(null);
            this.getBoardCellReference(destinationPosition).getWorker().addMove(destinationPosition);
        }

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
