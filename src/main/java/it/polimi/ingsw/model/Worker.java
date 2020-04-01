package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Single worker assigned to a player
 */
public class Worker {

    private ArrayList<Position> moves;
    private ArrayList<Position> builds;
    private Position initialPosition;
    private Card card;

    public Worker(Card card){
        this.card = card;
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


    public Card getCard() {
        return player.getCard();
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
        Card card = player.getCard();
        BlockStrategy blockStrategy = this.player.getGame().getPreviousTurn().getCurrentPlayer().getCard().getBlockStrategy();
        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(this, cell);
        boolean isValidPush = opponentStrategy.isValidPush(this, cell);

        if( !blockStrategy.isValidMoveForNextPlayer(this, cell)) throw new BlockedMoveException();
        else if( !moveStrategy.isAllowedToMove(this) ) throw new NotAllowedMoveException();
        else if ( !isValidMove || !isValidPush ) throw new NotValidMoveException();
            else {
                    opponentStrategy.pushOpponent(this, cell);
                    this.updatePosition(cell);
        }
    }

    public void build(BoardCell cell){

    }

    public void forceMove(BoardCell cell){

    }
}
