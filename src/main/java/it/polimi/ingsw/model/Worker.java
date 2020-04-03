package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * Single worker assigned to a player
 */
public class Worker {

    private Position currentPosition;
    private ArrayList<Position> moves; // initial positoin here
    private ArrayList<Position> builds; //initial position here
    private ArrayList<Operation> operations;
    private Card card;

    public Worker(){
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
    }

    public int getNumMoves(){
        return moves.size();
    }

    public int getNumBuilds(){
        return builds.size();
    }

    public Position getCurrentPosition(){
        return this.currentPosition;
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

    //to delete
    /*
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

     */

    public void addMoves(Position position) {
        moves.add(position);
    }

    public void setCurrentPosition(Position position) {
        this.currentPosition = position;
    }
}
