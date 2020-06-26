package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

/**
 * defines a generic game Turn
 */
public abstract class Turn implements Serializable {
    final Player currentPlayer;
    protected boolean isUndoAvailable = false; // false when deserializing for undo
    final private TurnPhase phase;
    protected transient ScheduledFuture<?> undoTimer;

    public Turn(TurnPhase turnPhase, Player currentPlayer){
        this.phase = turnPhase;
        this.currentPlayer = currentPlayer;

    }
    public TurnPhase getPhase(){
        return this.phase;
    }

    /**
     * property got from MoveStrategy which specifies if a worker is allowed to move at a certain moment
     * @return true if current player's worker is allowed to move at the moment
     */
    public boolean isAllowedToMove(){
        return false;
    }

    /**
     * property got from MoveStrategy which specifies if a worker is allowed to move at a certain moment
     * @param workerPosition position where a worker is currently located
     * @return true if the worker located in workerPosition is allowed to move at the moment
     */
    public boolean isAllowedToMove(Position workerPosition){
        return false;
    }

    /**
     * property got from MoveStrategy which specifies if a worker is required to move at a certain moment
     * @return true if current player's worker is required to move at the moment
     */
    public boolean isRequiredToMove(){
        return false;
    }

    /**
     * property got from MoveStrategy which specifies if a worker is required to move at a certain moment
     * @param workerPosition position where a worker is currently located
     * @return true if the worker located in workerPosition is required to move at the moment
     */
    public boolean isRequiredToMove(Position workerPosition){
        return false;
    }

    /**
     * property got from BuildStrategy which specifies if a worker is required to build at a certain moment
     * @return true if current player's worker is required to build at the moment
     */
    public boolean isRequiredToBuild(){
        return false;
    }

    /**
     * property got from BuildStrategy which specifies if a worker is required to build at a certain moment
     * @param workerPosition position where a worker is currently located
     * @return true if the worker located in workerPosition is required to build at the moment
     */
    public boolean isRequiredToBuild(Position workerPosition){
        return false;
    }

    /**
     * property got from BuildStrategy which specifies if a worker is allowed to build at a certain moment
     * @return true if current player's worker is allowed to build at the moment
     */
    public boolean isAllowedToBuild(){
        return false;
    }

    /**
     * property got from BuildStrategy which specifies if a worker is allowed to build at a certain moment
     * @param workerPosition position where a worker is currently located
     * @return true if the worker located in workerPosition is allowed to build at the moment
     */
    public boolean isAllowedToBuild(Position workerPosition){
        return false;
    }

    /**
     * @param board main board of the game
     * @param workerPosition position where a worker is currently located
     * @return true if worker in workerPosition can build at the moment
     */
    protected boolean canBuild(Board board, Position workerPosition){
        return false;
    }

    /**
     * @param board main board of the game
     * @param workerPosition position where a worker is currently located
     * @return true if worker in workerPosition can move at the moment
     */
    protected boolean canMove(Board board, Position workerPosition){
        return false;
    }

    /**
     * @return true if current player hasn't placed all his workers yet
     */
    public boolean isAnyWorkerNotPlaced(){
        return currentPlayer.isAnyWorkerNotPlaced();
    }

    /**
     * checks if a move from start position to destination position is feasible
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where a worker is moving to
     * @return true if a movement is feasible from start to destinaition position
     */
    public boolean isFeasibleMove(Board board, Position startPosition, Position destinationPosition){
        return false;
    }

    /**
     * checks if a build from start position to destination position is feasible
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where a worker is building at
     * @param isDome true if worker is building a dome
     * @return true if a building at destination position is feasible
     */
    public boolean isFeasibleBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){
        return false;
    }

    /**
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where worker located in start position is moving to
     * @return true if a movement from start to destination position is blocked
     */
    public boolean isBlockedMove(Board board, Position startPosition, Position destinationPosition) {
        return false;
    }

    /**
     * @return true if previus player's movement blocks current player
     */
    public boolean getPreviousBlockNextPlayer(){
        return false;
    }

    /**
     * @return true if current player's movement blocks next player
     */
    public boolean getBlockNextPlayer(){
        return false;
    }

    /**
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where the worker located in start position is moving to
     * @return position where the worker currently located in destination position is going to be pushed
     */
    protected Position boardMove(Board board, Position startPosition, Position destinationPosition){
        return null;
    };

    /**
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where the worker located at start position is building at
     * @param isDome true if the worker is building a dome
     */
    protected void boardBuild(Board board, Position startPosition, Position destinationPosition, boolean isDome){};

    /**
     * @param board main board of the game
     * @param placePosition position where a worker is placed
     */
    protected Optional<Integer> boardPlace(Board board, Position placePosition){return Optional.empty();};

    public Card getPreviousTurnCard(){return null;}

    public boolean isCurrentPlayerUUID(UUID playerId){
        return currentPlayer.getUuid() == playerId;
    }
    protected Player getCurrentPlayer() {
        return currentPlayer;
    }
    public boolean isSetCurrentWorker(){
        return false;
    }
    public boolean isBlockNextPlayer() {
        return false;
    }

    /**
     * checks if a movement from start to destination position is a winning move
     * @param board main board of the game
     * @param startPosition position where a worker is located
     * @param destinationPosition position where the worker located at start position is moving to
     * @return true if a movement from start to destination position is a winning move
     */
    public boolean isWinningMove(Board board, Position startPosition, Position destinationPosition){
       return false;
    }

    /**
     * checks if worker placed in workerPosition is current player's current worker
     * @param workerPosition position where a worker is located
     * @return true if worker located at workerPosition is current player's current worker
     */
    public boolean checkCurrentWorker(Position workerPosition){
        return false;
    }

    /**
     * checks all workers on the board in order to establish if there's a loose condition on a player
     * @param board main board of the game
     * @return true if a player has all his workers blocked and has lost the game
     */
    boolean isLoseCondition(Board board) {
        return false;
    }

    /**
     * checks if a worker is required to move or build but can't do neither of them
     * @param board main board of the game
     * @param workerPosition position where a worker is located
     * @return true worker is in a situation where can't move neither build
     */
    boolean cannotMakeRequiredOperation(Board board, Position workerPosition){
        boolean isRequiredToMove = isRequiredToMove(workerPosition);
        boolean isRequiredToBuild = isRequiredToBuild(workerPosition);

        if(isRequiredToBuild && isRequiredToMove){
            if(!canBuild(board, workerPosition) && !canMove(board, workerPosition))
                return true;
        }else if(isRequiredToBuild){
            if(!canBuild(board, workerPosition))
                return true;
        }else if(isRequiredToMove){//should be impossible
            if(!canMove(board, workerPosition))
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Turn turn = (Turn) o;
        return /*isUndoAvailable == turn.isUndoAvailable &&*/
                Objects.equals(getCurrentPlayer(), turn.getCurrentPlayer()) &&
                getPhase() == turn.getPhase();
    }

}
