package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.BlockedMoveException;
import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.BuildStrategy;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.WinStrategy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Turn implements Serializable {
    private final Player currentPlayer;
    private int currentWorkerId;
    private Card previousTurnCard;
    private List<Board> undoBoards;
    private Board board;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

//    public Turn(Player currentPlayer){
//        this.currentPlayer = currentPlayer;
//        //this.currentWorkerId = currentWorkerId;
//        this.currentWorkerId = -1;
//        this.blockNextPlayer = false;
//
//    }


    public Turn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer){
        this.currentPlayer = currentPlayer;
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
    }
    public boolean isAllowedToMove(){
        return isAllowedToMove(currentWorkerId);
    }
    public boolean isAllowedToMove(int workerId){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        return isAllowedToMove;
    }
    public boolean isRequiredToMove(){
        return isRequiredToMove(currentWorkerId);
    }
    public boolean isRequiredToMove(int workerId){
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }

    public boolean isRequiredToBuild(){
        return isRequiredToBuild(currentWorkerId);
    }
    public boolean isRequiredToBuild(int workerId){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }

    public boolean isAllowedToBuild(){
        return isAllowedToBuild(currentWorkerId);
    }
    public boolean isAllowedToBuild(int workerId){
        if(!isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        return isAllowedToBuild;
    }

    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

    public int place(Position placePosition){
        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition))
            return currentPlayer.addWorker(newWorker);
        else
            return -1;
    }

    public void move(int workerId, Position destinationPosition) throws PositionOutOfBoundsException, InvalidPushCell{
        Card card = currentPlayer.getCard();
       Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);

        if(blockNextPlayer == false) {
            blockNextPlayer(workerId, destinationPosition);
        }
        Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
        board.putWorkers(startPosition, destinationPosition, pushDestPosition);

    }

    public boolean isFeasibleMove(int workerId, Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        boolean isOwnWorker = false;
        for(int i=0; i<currentPlayer.getNumWorkers(); i++) {
            if(currentPlayer.getWorkerCurrentPosition(i).equals(destinationPosition))
                isOwnWorker = true;
        }

        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, this.board);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, isOwnWorker, this.board);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }

    public boolean isFeasibleBuild(int workerId, Position destinationPosition, boolean isDome){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        BuildStrategy buildStrategy = card.getBuildStrategy();
        boolean isValidBuild = buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, this.board);

        return isValidBuild;
    }

    public boolean isBlockedMove(int workerId, Position destinationPosition){
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();
        return card.getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }
    public boolean getPreviousBlockNextPlayer(){
        return previousBlockNextPlayer;
    }


    public boolean blockNextPlayer(int workerId, Position destinationPosition) {
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();

        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

//
//    public boolean build (Position destinationPosition, boolean isDome) {
//        if(!isWorkersPositionSet())
//            return false;
//        Card card = currentPlayer.getCard();
//        int numBuilds = currentPlayer.getNumBuildsWorker(currentWorkerId);
//        int numMoves = currentPlayer.getNumMovesWorker(currentWorkerId);
//        Operation lastOperation = currentPlayer.getLastOperationWorker(currentWorkerId);
//        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
//        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
//        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
//        boolean canBuild = board.canBuild(startPosition, destinationPosition, card, isDome);
//
//        if( canBuild == true ){
//            board.build(startPosition, destinationPosition, isDome);
//            return true;
//        }else
//            return falisWorkersPositionSetse;
//
//    }

    public void build (Position destinationPosition, boolean isDome){
        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
        board.build(startPosition, destinationPosition, isDome);
    }

    public boolean isWinningMove(Position destinationPosition){
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(currentWorkerId);
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, this.board );
    }

    public boolean checkLoseCondition() {
        Card card = currentPlayer.getCard();
        ArrayList<Position> currentPositions = new ArrayList<>();
        boolean loseCondition = true;
        if(isSetCurrentWorker()){
            loseCondition = !canDoOperation(currentWorkerId); //&& loseCondition

        }else {//first operation of the turn can have workerId not set
            for (int workerId = 0; workerId < currentPlayer.getNumWorkers(); workerId++) {
                loseCondition = !canDoOperation(workerId) && loseCondition;
            }
        }
        return loseCondition;
    }

    private boolean canDoOperation(int workerId){
        boolean isRequiredToMove = isRequiredToMove(workerId);
        boolean isRequiredToBuild = isRequiredToBuild(workerId);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(workerId) && !canMove(workerId))
                return false;
        }else if(isRequiredToBuild){
            if(!canBuild(workerId))
                return false;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(workerId))
                return false;
        }
        return true;
    }

    /**
     * Checks is the chosen worker can move in any cell in the current operation, respecting all constraints imposed by cards
     * @param workerId WorkerId of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can move in the current operation
     */
    private boolean canMove(int workerId) {
        Position position = currentPlayer.getWorkerCurrentPosition(workerId);
        int currentY = position.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = position.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);

                    //try {
                        //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                        if(!isBlockedMove(workerId, destPosition) && isFeasibleMove(workerId, destPosition))
                            return true;
                    //}catch (BlockedMoveException e){
                    //    continue;
                    //}
                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }
    /**
     * Checks is the chosen worker can build in any cell in the current operation, respecting all constraints imposed by cards
     * @param workerId WorkerId of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can build in the current operation
     */
    private boolean canBuild(int workerId){
        Position position = currentPlayer.getWorkerCurrentPosition(workerId);
        int currentY = position.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = position.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);

                   //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                    if(isFeasibleBuild(workerId, destPosition, false) && isFeasibleBuild(workerId, destPosition, true))
                        return true;

                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }

    public boolean checkPlayer(Player viewPlayer){
        return currentPlayer.getUuid() == viewPlayer.getUuid();
    }

//     private Player getCurrentPlayer() {
////            return currentPlayer;
////        }

    public UUID getCurrentPlayerUUID() {
        return currentPlayer.getUuid();
    }

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    public boolean isSetCurrentWorker(){
        return currentWorkerId>=0;
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }


    public boolean updateCurrentWorker(int currentWorkerId){
        if(!isSetCurrentWorker()){
            setCurrentWorker(currentWorkerId);
            return true;
        }else
            return false;
    }

    private void setCurrentWorker(int currentWorkerId){
        if(currentPlayer.isWorkerSet(currentWorkerId))
            this.currentWorkerId=currentWorkerId;
    }

    private void saveBoard(){
        undoBoards.add(board);
        board = board.clone();
    }

    public Card getPlayerCard(){
        return currentPlayer.getCard();
    }
}
