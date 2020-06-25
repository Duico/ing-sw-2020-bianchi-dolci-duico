package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.InvalidPushCell;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.model.strategy.BuildStrategy;
import it.polimi.ingsw.model.strategy.MoveStrategy;
import it.polimi.ingsw.model.strategy.OpponentStrategy;
import it.polimi.ingsw.model.strategy.WinStrategy;

/**
 * Represent actual normal turn in Game where player can do movements and buildings with their workers
 */
public class NormalTurn extends Turn {
    private Optional<Integer> currentWorkerId;
    private Card previousTurnCard;
    //private Card previousBlockCard;
    private boolean blockNextPlayer;
    private boolean previousBlockNextPlayer;

    public NormalTurn(Player currentPlayer, Card previousTurnCard, /*Card previousBlockCard,*/ boolean previousBlockNextPlayer){
        super(TurnPhase.NORMAL, currentPlayer);
        this.currentWorkerId = Optional.empty();
        this.blockNextPlayer = false;
        this.previousTurnCard = previousTurnCard;
        //this.previousBlockCard = previousBlockCard;
        this.previousBlockNextPlayer = previousBlockNextPlayer;
    }

    @Override
    public boolean isAllowedToMove(){
        if(currentWorkerId.isPresent()){
            return isAllowedToMove(currentPlayer.getWorkerPosition(currentWorkerId.get()));
        }else{
            Card card = currentPlayer.getCard();
            boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(0, 0);
            return isAllowedToMove;
        }
    }

    @Override
    public boolean isAllowedToMove(Position workerPosition){

        Card card = currentPlayer.getCard();
        Optional<Integer> workerId = currentPlayer.getWorkerId(workerPosition);
        if(!workerId.isPresent()) {
            return false;
        }
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId.get());
        int numMoves = currentPlayer.getNumMovesWorker(workerId.get());
        boolean isAllowedToMove = card.getMoveStrategy().isAllowedToMove(numMoves, numBuilds);
        return isAllowedToMove;
    }

    @Override
    public boolean isRequiredToMove(){
        if(currentWorkerId.isPresent()){
            return isRequiredToMove(currentPlayer.getWorkerPosition(currentWorkerId.get()));
        }else {
            return true;
        }
    }

    @Override
    public boolean isRequiredToMove(Position workerPosition){
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        Optional<Integer> workerId = currentPlayer.getWorkerId(workerPosition);
        if(!workerId.isPresent())
            return false;
        //int numBuilds = currentPlayer.getNumBuildsWorker(workerId);
        int numMoves = currentPlayer.getNumMovesWorker(workerId.get());
        boolean isRequiredToMove = card.getMoveStrategy().isRequiredToMove(numMoves);
        return isRequiredToMove;
    }

    @Override
    public boolean isRequiredToBuild(){
        if(currentWorkerId.isPresent()){
            return isRequiredToBuild(currentPlayer.getWorkerPosition(currentWorkerId.get()));
        }else {
            return true;
        }
    }

    @Override
    public boolean isRequiredToBuild(Position workerPosition){
        if(isAnyWorkerNotPlaced())
            return false;
        Card card = currentPlayer.getCard();
        Optional<Integer> workerId = currentPlayer.getWorkerId(workerPosition);
        if(!workerId.isPresent())
            return false;
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId.get());
        int numMoves = currentPlayer.getNumMovesWorker(workerId.get());
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId.get());
        boolean isRequiredToBuild = card.getBuildStrategy().isRequiredToBuild(numMoves, numBuilds, lastOperation);
        return isRequiredToBuild;
    }

    @Override
    public boolean isAllowedToBuild(){
    //TODO improve
      if(currentWorkerId.isPresent()) {
          return isAllowedToBuild(currentPlayer.getWorkerPosition(currentWorkerId.get()));
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
        Optional<Integer> workerId = currentPlayer.getWorkerId(workerPosition);
        if(!workerId.isPresent())
            return false;
        int numBuilds = currentPlayer.getNumBuildsWorker(workerId.get());
        int numMoves = currentPlayer.getNumMovesWorker(workerId.get());
        Operation lastOperation = currentPlayer.getLastOperationWorker(workerId.get());
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
    public Optional<Integer> getCurrentWorkerId(){
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


    /**
     * check if current player's worker is located in the exact same position as parameter position
     * @param workerPosition position to check
     * @return true if current player's worker position is same as parameter position
     */
    public boolean checkCurrentWorker(Position workerPosition){
        if(isSetCurrentWorker())
            return workerPosition != null && workerPosition.equals(currentPlayer.getWorkerPosition(currentWorkerId.get()));
        else{
            for(int i=0;i<currentPlayer.getNumWorkers();i++){
                if(currentPlayer.getWorkerPosition(i).equals(workerPosition))
                    return true;
            }
            return false;
        }

    }

    public boolean isSetCurrentWorker(){
        return currentWorkerId.isPresent();
    }

    public boolean isBlockNextPlayer() {
        return blockNextPlayer;
    }


    /**
     * set current worker ID if it has not been set yet
     * @param workerId worker ID to set at current player
     * @return true if current worker ID is successfully set
     */
    public boolean updateCurrentWorker(Integer workerId){
        if(!isSetCurrentWorker()){
            setCurrentWorker(workerId);
            return true;
        }else
            return false;
    }

    private void setCurrentWorker(Integer workerId){
        if(currentPlayer.isWorkerSet(workerId))
            this.currentWorkerId = Optional.of(workerId);
    }

    public Card getPreviousTurnCard(){
        return this.previousTurnCard;
    }

    /**
     * moves worker placed in start position to destination position checking if destination position
     * BoardCell is already occupied by a worker, in that case returns position where this last one is going to
     * be pushed
     * @param board Board of the Game
     * @param startPosition position from which a movement is done
     * @param destinationPosition position where the worker in start position is moving to
     * @return position where worker placed in destination position is pushed to
     */
    protected Position boardMove(Board board, Position startPosition, Position destinationPosition) {
        Player currentPlayer = this.getCurrentPlayer();
        Optional<Integer> workerId = currentPlayer.getWorkerId(startPosition);
        Card card = currentPlayer.getCard();

        if(!this.getBlockNextPlayer()) {
            blockNextPlayer=blockNextPlayer(board, startPosition, destinationPosition);
        }
        Position pushDestPosition;
        try {
            pushDestPosition = card.getOpponentStrategy().destinationPosition(startPosition, destinationPosition);
        }catch (InvalidPushCell e){
            pushDestPosition = null;
//            System.out.println("Invalid push cell");
        }
            board.putWorkers(startPosition, destinationPosition, pushDestPosition);
            this.updateCurrentWorker(workerId.get());
            return pushDestPosition;

    }


    /**
     * builds on Board at destination position
     * @param board Board of the game
     * @param startPosition position from which a building is done
     * @param destinationPosition position where the worker located in start position is building at
     * @param isDome specifies if the worker is building a dome
     */
    protected void boardBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        Optional<Integer> workerId = currentPlayer.getWorkerId(startPosition);
        board.build(startPosition, destinationPosition, isDome);
        this.updateCurrentWorker(workerId.get());
    }

    private boolean blockNextPlayer(Board board, Position startPosition, Position destinationPosition) {
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        return card.getBlockStrategy().blockNextPlayer(startPosition, destinationPosition, board);
    }

    /**
     * check current player's card strategies in order to verify a movement from start position to destination
     * position is feasible
     * @param board Board of the Game
     * @param startPosition position from which the current player is trying to move
     * @param destinationPosition position where the worker located in start position is trying to move to
     * @return true if movement from start position to destination position is correct and feasible
     */
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

    /**
     * check current player's card strategies in order to verify a building from start position to destination
     * position is feasible
     * @param board Board of the Game
     * @param startPosition position from which the current player is trying to build
     * @param destinationPosition position where the worker located in start position is trying to build at
     * @return true if building from start position to destination position is correct and feasible
     */
    public boolean isFeasibleBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();

        BuildStrategy buildStrategy = card.getBuildStrategy();
        boolean isValidBuild = buildStrategy.isValidBuild(startPosition, destinationPosition, isDome, board);

        return isValidBuild;
    }

    /**
     * checks if previous players'card strategies disabled a movement from start position to destination position
     * for other players
     * @param board Board of the game
     * @param startPosition position from which a movement has been selected
     * @param destinationPosition destination position of the selected movement
     * @return true if a movement from start to destination position is blocked by previous player
     */
    public boolean isBlockedMove(Board board, Position startPosition, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        return this.getPreviousTurnCard().getBlockStrategy().isBlockMove( startPosition, destinationPosition, board);
    }

    /**
     * checks if moving from start to destination position is a winning move
     * @param board Board of the game
     * @param startPosition position from which a movement is done
     * @param destinationPosition position where the worker located in start position is moved to
     * @return true if a movement from start to destination position is a winning move
     */
    public boolean isWinningMove(Board board, Position startPosition, Position destinationPosition){
        Player currentPlayer = this.getCurrentPlayer();
        Card card = currentPlayer.getCard();
        WinStrategy winStrategy = card.getWinStrategy();
        return winStrategy.isWinningMove(startPosition, destinationPosition, board);
    }

    /**
     * Checks if the chosen worker can move in any (adjacent) cell in the current operation, respecting all constraints imposed by cards
     * @param currentPosition Position of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can move in the current operation
     */
    protected boolean canMove(Board board, Position currentPosition) {
        if(!isAllowedToMove())
            return false;

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
     * Checks if the chosen worker can build in any (adjacent) cell in the current operation, respecting all constraints imposed by cards
     * @param currentPosition Position of the worker of currentPlayer you want to check
     * @return True if an adjacent cell exists where the player can build in the current operation
     */
    protected boolean canBuild(Board board, Position currentPosition){
        if(!isAllowedToBuild())
            return false;

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
        if(isSetCurrentWorker()){
            Position currentWorkerPosition = currentPlayer.getWorkerPosition(currentWorkerId.get());
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
