package it.polimi.ingsw.model;

public class PlaceWorkersTurn extends Turn {
    public PlaceWorkersTurn(Player currentPlayer) {
        super(TurnPhase.PLACE_WORKERS, currentPlayer);
    }

    int boardPlace(Board board, Position placePosition){
        Player currentPlayer = this.getCurrentPlayer();
        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            int workerId = currentPlayer.addWorker(newWorker);
            //comment for testing
            //emitEvent(new PlaceWorkerModelEvent(currentPlayer.getUuid(), workerId, placePosition));
            return workerId;
        }else
            return -1;
    }
}
