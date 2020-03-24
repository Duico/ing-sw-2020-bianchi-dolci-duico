package it.polimi.ingsw.model;

public class PlaceWorkersTurn extends Turn {
    public PlaceWorkersTurn(Player currentPlayer) {
        super(TurnPhase.PLACE_WORKERS, currentPlayer);
    }

    int boardPlace(Board board, Position placePosition){
        Player currentPlayer = this.getCurrentPlayer();
        //for extra security
        if(!currentPlayer.isAnyWorkerNotPlaced()){
            return -1;
        }

        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            int workerId = currentPlayer.addWorker(newWorker);
            //comment for testing
            //emitEvent(new PlaceWorkerModelEvent(currentPlayer.getUuid(), workerId, placePosition));
            if(workerId<0)
                throw new RuntimeException("Worker set in the board without reference from player");
            return workerId;
        }

        return -1;
    }
}


