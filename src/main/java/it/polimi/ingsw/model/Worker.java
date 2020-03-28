package it.polimi.ingsw.model;

/**
 * Single worker assigned to a player
 */
public class Worker {
    /**
     * Current position of the player
     */
    private BoardCell cell;
    /**
     * Owner of this worker
     */
    private final Player player;

    /**
     * Card assigned to the player, which contains strategies to be used by the worker
     */
    private Card card;
    /**
     * Current turn of the player
     */
    private Turn turn;

    /**
     * Creates a new worker assigned to the desired player
     * @param player Player who owns the worker
     */
    public Worker(Player player){
        this.player = player;
        this.card = player.getCard();
    }

    /**
     * Places the worker in the initial cell<br/>
     * Doesn't update reference to a worker of the previous cell
     * @param cell Cell to move into
     * @throws OccupiedCellException If cell is already occupied
     */
    public void placeWorker(BoardCell cell) throws OccupiedCellException{
        if(cell.getWorker() != null) throw new OccupiedCellException();
        else{
            cell.setWorker(this);
            this.cell = cell;
        }
    }

    /**
     * Set player position and updates worker in the new cell, <b>without</b> changing references in the previous cell
     * @param cell Cell to move to
     */
    private void updatePosition(BoardCell cell){
        this.setCell(cell);
        this.cell.setWorker(this);
    }

    public BoardCell getCell() {
        return cell;
    }

    public void setCell(BoardCell cell) {
        this.cell = cell;
    }

    public Player getPlayer() {
        return player;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = this.player.getGame().getTurn();
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = this.player.getCard();
    }

//    public List<Operation> play() {
//        MoveStrategy moveStrategy = card.getMoveStrategy();
//        BuildStrategy buildStrategy = card.getBuildStrategy();
//        boolean isRequiredToMove = moveStrategy.isRequiredToMove(this);
//        boolean isRequiredToBuild = buildStrategy.isRequiredToBuild(this);
//        boolean isAllowedToMove = moveStrategy.isAllowedToMove(this);
//        boolean isAllowedToBuild = buildStrategy.isAllowedToBuild(this);
//
//
//        while (isRequiredToMove || isRequiredToBuild) {
//            //make a choice between move and build
//
//            if (isAllowedToMove && isAllowedToBuild) {
//                //can use both cursors
//
//            } else if (isAllowedToMove) {
//                //change cursor to move
//
//            } else if (isAllowedToBuild) {
//                //change cursor to build
//
//            } else {
//                //undo or lose game
//            }
//
//        }
//
//        while (isAllowedToMove || isAllowedToBuild) {
//            //user is able to skip to next turn
//
//            if (isAllowedToMove && isAllowedToBuild) {
//                //can use both cursors
//            } else if (isAllowedToMove) {
//                //change cursor to move
//
//            } else if (isAllowedToBuild) {
//                //change cursor to build
//
//            } else {
//                //next turn
//            }
//        }
//
//
//
//    }

    public void move(BoardCell cell) throws BlockedMoveException, NotAllowedMoveException, NotValidMoveException{
        BlockStrategy blockStrategy = this.player.getGame().getPreviousTurn().getCurrentPlayer().getCard().getBlockStrategy();
        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(this, cell);
        boolean isValidPush = opponentStrategy.isValidPush(this, cell);

        if( !blockStrategy.isValidMoveForNextPlayer(this, cell)) throw new BlockedMoveException();
        else if( !moveStrategy.isAllowedToMove(this) ) throw new NotAllowedMoveException();
        else if ( !isValidMove || !isValidPush ) {
            throw new NotValidMoveException();
        }else {
                /*
                    pushOpponent
                    move
                 */
        }
    }

    public void build(BoardCell cell){

    }

    public void forceMove(BoardCell cell){

    }
}
