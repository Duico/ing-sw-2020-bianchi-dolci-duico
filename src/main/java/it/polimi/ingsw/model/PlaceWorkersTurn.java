package it.polimi.ingsw.model;

/**
 *  Represent a specific type of game Turn on which players are allowed just to place their workers on the Board
 */
public class PlaceWorkersTurn extends Turn {

    public PlaceWorkersTurn(Player currentPlayer) {
        super(TurnPhase.PLACE_WORKERS, currentPlayer);
    }

    /**
     * places worker on Board at place position if current player has not placed both his workers yet
     * @param board Board of the game
     * @param placePosition position where a worker is placed
     * @return workerID if worker is placed
     */
    protected Optional<Integer> boardPlace(Board board, Position placePosition){
        Player currentPlayer = this.getCurrentPlayer();
        //for extra security
        if(!currentPlayer.isAnyWorkerNotPlaced()){
            return Optional.empty();
        }

        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            Optional<Integer> workerId = currentPlayer.addWorker(newWorker);
            if(!workerId.isPresent())
                throw new RuntimeException("Worker set in the board without reference from player");
            return workerId;
        }

        return Optional.empty();
    }


}


