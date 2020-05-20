package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.BuildStrategy;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.WinStrategy;

public class NormalTurn extends Turn {
    private int currentWorkerId;
    private Card previousTurnCard;
    //private Card previousBlockCard;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

    public NormalTurn(Player currentPlayer, Card previousTurnCard, /*Card previousBlockCard,*/ boolean previousBlockNextPlayer){
        super(TurnPhase.NORMAL, currentPlayer);
        this.currentWorkerId = -1;
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        //this.previousBlockCard = previousBlockCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
    }

    @Override
    public boolean isAllowedToMove(){
        //TODO not the best solution
        if(currentWorkerId >=0){
            return isAllowedToMove(currentPlayer.getWorkerPosition(currentWorkerId));
        }else{
            Card card = currentPlayer.getCard();
            boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(0, 0);
            return isAllowedToMove;
        }
    }

    @Override
    public boolean isAllowedToMove(Position workerPosition){

        Card card = currentPlayer.getCard();
        int workerId = currentPlayer.getWorkerId(workerPosition);
        if(workerId == -1) {
            return false;
        }
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        return isAllowedToMove;
    }

    @Override
    public boolean isRequiredToMove(){
        if(currentWorkerId>=0){
            return isRequiredToMove(currentPlayer.getWorkerPosition(currentWorkerId));
        }else {
            return true;
        }
    }

    @Override
    public boolean isRequiredToMove(Position workerPosition){
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int workerId = currentPlayer.getWorkerId(workerPosition);
        if(workerId == -1)
            return false;
        //int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        System.out.println(card.getName());
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }

    @Override
    public boolean isRequiredToBuild(){
        if(currentWorkerId>=0){
            return isRequiredToBuild(currentPlayer.getWorkerPosition(currentWorkerId));
        }else {
            return true;
        }
    }

    @Override
    public boolean isRequiredToBuild(Position workerPosition){
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int workerId = currentPlayer.getWorkerId(workerPosition);
        if(workerId == -1)
            return false;
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId);
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId);
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }

    @Override
    public boolean isAllowedToBuild(){
    //TODO improve
      if(currentWorkerId >=0) {
          return isAllowedToBuild(currentPlayer.getWorkerPosition(currentWorkerId));
      }else {
          Card card = currentPlayer.getCard();
          //any operation is the same
          boolean isAllowedToBuild = card.getBuildStrategy().isAllowedToBuild(0, 0, Operation.MOVE);
          return isAllowedToBuild;
      }
    }

    @Override
    public boolean isAllowedToBuild(Position workerPosition){
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        int workerId = currentPlayer.getWorkerId(workerPosition);
        if(workerId == -1)
            return false;
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
    //public Card getPreviousBlockCard(){return previousBlockCard;}
    public int getCurrentWorkerId(){
        return this.currentWorkerId;
    }

    //TODO delete: used only in tests
//    public boolean checkCurrentWorker(int workerId){
//        return  isSetCurrentWorker() && (currentWorkerId == workerId );
//    }

    /*@Override
    public boolean checkCurrentWorker(Position workerPosition){
        return workerPosition != null && workerPosition == currentPlayer.getWorkerPosition(currentWorkerId);
    }*/


    public boolean checkCurrentWorker(Position workerPosition){
        if(isSetCurrentWorker())
            return workerPosition != null && workerPosition.equals(currentPlayer.getWorkerPosition(currentWorkerId));
        else{
            for(int i=0;i<currentPlayer.getNumWorkers();i++){
                if(currentPlayer.getWorkerPosition(i).equals(workerPosition))
                    return true;
            }
            return false;
        }

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

    protected Position boardMove(Board board, Position startPosition, Position destinationPosition) {
        Player currentPlayer = this.getCurrentPlayer();
        int workerId = currentPlayer.getWorkerId(startPosition);
        Card card = currentPlayer.getCard();

        if(this.getBlockNextPlayer() == false) {
            blockNextPlayer=blockNextPlayer(board, startPosition, destinationPosition);
        }
        try {
            Position pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
            board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            this.updateCurrentWorker(workerId);
            return pushDestPosition;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    protected void boardBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        int workerId = currentPlayer.getWorkerId(startPosition);
        board.build(startPosition, destinationPosition, isDome);
        this.updateCurrentWorker(workerId);
    }

    private boolean blockNextPlayer(Board board, Position startPosition, Position destinationPosition) {
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    public boolean isFeasibleMove(Board board, Position startPosition, Position destinationPosition){

        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();

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

    public boolean isFeasibleBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();

        BuildStrategy buildStrategy = card.getBuildStrategy();
        boolean isValidBuild = buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, board);

        return isValidBuild;
    }

    /*public boolean isBlockedMove(Board board, Position startPosition, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        return this.getPreviousTurnCard().getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }*/

    public boolean isBlockedMove(Board board, Position startPosition, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        return this.getPreviousTurnCard().getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }

    public boolean isWinningMove(Board board, Position startPosition, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, board);
    }

    /**
     * Checks is the chosen worker can move in any cell in the current operation, respecting all constraints imposed by cards
     * @param currentPosition Position of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can move in the current operation
     */
    protected boolean canMove(Board board, Position currentPosition) {
        Player currentPlayer = this.getCurrentPlayer();

        int currentY = currentPosition.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = currentPosition.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);
                    //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                    if(!isBlockedMove(board, currentPosition, destPosition) && isFeasibleMove(board, currentPosition, destPosition)) {
                        return true;
                    }

                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }
    /**
     * Checks if the chosen worker can build in any cell in the current operation, respecting all constraints imposed by cards
     * @param currentPosition Position of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can build in the current operation
     */
    protected boolean canBuild(Board board, Position currentPosition){
        int currentY = currentPosition.getY();
        for(int dy= -1; dy<=1; dy++) {
            int positionY=currentY+dy;
            if (positionY < 0 || positionY >= Position.height) {
                continue;
            }
            int currentX = currentPosition.getX();
            for (int dx = -1; dx <=1; dx++) {
                int positionX=currentX+dx;
                if (positionX < 0 || positionX >= Position.width) {
                    continue;
                }
                try {
                    Position destPosition = new Position(positionX, positionY);

                    //if destPosition is a good candidate for a move, check if the worker can effectively move in destPosition
                    if(isFeasibleBuild(board, currentPosition, destPosition, false) || isFeasibleBuild(board, currentPosition, destPosition, true)) {
                        return true;
                    }
                }catch(PositionOutOfBoundsException e){
                    continue;
                }

            }
        }
        return false;
    }

    @Override
    boolean isLoseCondition(Board board) {
        Player currentPlayer = this.getCurrentPlayer();
        boolean loseCondition = true;
        if(this.isSetCurrentWorker()){
            Position currentWorkerPosition = currentPlayer.getWorkerPosition(currentWorkerId);
            loseCondition = cannotMakeRequiredOperation(board, currentWorkerPosition); //&& loseCondition

        }else {//first operation of the turn can have workerId not set
            for (int workerId = 0; workerId < currentPlayer.getNumWorkers(); workerId++) {
                Position workerPosition = currentPlayer.getWorkerPosition(workerId);
                loseCondition = cannotMakeRequiredOperation(board, workerPosition);
                if(!loseCondition)
                    return false;
            }
        }
        return loseCondition;
    }

}
