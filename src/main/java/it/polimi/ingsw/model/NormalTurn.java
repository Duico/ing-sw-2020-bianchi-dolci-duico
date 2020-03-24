package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.BuildStrategy;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.WinStrategy;

public class NormalTurn extends Turn {
    private int currentWorkerId;
    private Card previousTurnCard;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

    public NormalTurn(Player currentPlayer, Card previousTurnCard, boolean previousBlockNextPlayer){
        super(TurnPhase.NORMAL, currentPlayer);
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
        if(currentWorkerId>=0){
            return isRequiredToMove(currentWorkerId);
        }else {
            return true;
        }
    }
    public boolean isRequiredToMove(int workerId){
        Card card = currentPlayer.getCard();
        //int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }

    public boolean isRequiredToBuild(){
        if(currentWorkerId>=0){
            return isRequiredToBuild(currentWorkerId);
        }else {
            return true;
        }
    }
    public boolean isRequiredToBuild(int workerId){
        if(isAnyWorkerNotPlaced())
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
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(numMoves, numBuilds, lastOperation);
        return isAllowedToBuild;
    }

    public boolean getBlockNextPlayer(){
        return blockNextPlayer;
    }
    public boolean getPreviousBlockNextPlayer(){
        return previousBlockNextPlayer;
    }

    public boolean checkPlayer(Player viewPlayer){
        return currentPlayer.getUuid() == viewPlayer.getUuid();
    }

//     private Player getCurrentPlayer() {
////            return currentPlayer;
////        }

    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    public boolean checkCurrentWorker(int workerId){
        return  !isSetCurrentWorker() || (currentWorkerId == workerId );
    }

    public boolean isSetCurrentWorker(){
        return currentWorkerId>=0;
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }


    public boolean updateCurrentWorker(int workerId){
        if(!isSetCurrentWorker()){
            setCurrentWorker(workerId);
            return true;
        }else
            return false;
    }

    private void setCurrentWorker(int workerId){
        if(currentPlayer.isWorkerSet(workerId))
            this.currentWorkerId=workerId;
    }


    public Card getPreviousTurnCard(){
        return this.previousTurnCard;
    }

    //NEW ADDS
    protected void boardMove(Board board, int workerId, Position destinationPosition) {
        updateCurrentWorker(workerId);
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(this.getCurrentWorkerId());

        if(this.getBlockNextPlayer() == false) {
            blockNextPlayer(board, workerId, destinationPosition);
        }
        try {
            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            //comment for testing
            //emitEvent(new PutWorkerModelEvent(currentPlayer.getUuid(), workerId, startPosition, destinationPosition, pushDestPosition));
            this.updateCurrentWorker(workerId);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    protected void boardBuild(Board board, int workerId, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(this.getCurrentWorkerId());
        board.build(startPosition, destinationPosition, isDome);
        //comment for testing
        //emitEvent(new BuildWorkerModelEvent(currentPlayer.getUuid(), workerId, startPosition, destinationPosition) );
        this.updateCurrentWorker(workerId);
    }
    private boolean blockNextPlayer(Board board, int workerId, Position destinationPosition) {
        Player currentPlayer = this.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);
        Card card = currentPlayer.getCard();

        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    public boolean isFeasibleMove(Board board, int workerId, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        boolean isOwnWorker = currentPlayer.isOwnWorkerInPosition(destinationPosition);

        MoveStrategy moveStrategy = card.getMoveStrategy();
        OpponentStrategy opponentStrategy = card.getOpponentStrategy();
        boolean isValidMove = moveStrategy.isValidMove(startPosition, destinationPosition, board);
        boolean isValidPush = opponentStrategy.isValidPush(startPosition, destinationPosition, isOwnWorker, board);

        if( isValidMove == true && isValidPush==true ) {
            return true;
        }else{
            return false;
        }
    }

    public boolean isFeasibleBuild(Board board, int workerId, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        BuildStrategy buildStrategy = card.getBuildStrategy();
        boolean isValidBuild = buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, board);

        return isValidBuild;
    }

    public boolean isBlockedMove(Board board, int workerId, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(workerId);

        return this.getPreviousTurnCard().getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }

    public boolean isWinningMove(Board board, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        Position startPosition = currentPlayer.getWorkerCurrentPosition(this.getCurrentWorkerId());
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, board);
    }

    /**
     * Checks is the chosen worker can move in any cell in the current operation, respecting all constraints imposed by cards
     * @param workerId WorkerId of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can move in the current operation
     */
    protected boolean canMove(Board board, int workerId) {
        Player currentPlayer = this.getCurrentPlayer();
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
                    if(!isBlockedMove(board, workerId, destPosition) && isFeasibleMove(board, workerId, destPosition))
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
    protected boolean canBuild(Board board, int workerId){
        Player currentPlayer = this.getCurrentPlayer();
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
                    if(isFeasibleBuild(board, workerId, destPosition, false) && isFeasibleBuild(board, workerId, destPosition, true))
                        return true;

                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }

    boolean isLoseCondition(Board board) {
        Player currentPlayer = this.getCurrentPlayer();
        boolean loseCondition = true;
        if(this.isSetCurrentWorker()){
            loseCondition = cannotMakeRequiredOperation(board, this.getCurrentWorkerId()); //&& loseCondition

        }else {//first operation of the turn can have workerId not set
            for (int workerId = 0; workerId < currentPlayer.getNumWorkers(); workerId++) {
                loseCondition = cannotMakeRequiredOperation(board, workerId);
                if(!loseCondition)
                    return false;
            }
        }
        return loseCondition;
    }

}
