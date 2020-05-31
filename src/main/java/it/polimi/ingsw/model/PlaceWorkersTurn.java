package it.polimi.ingsw.model;

public class PlaceWorkersTurn extends Turn {

    public PlaceWorkersTurn(Player currentPlayer) {
        super(TurnPhase.PLACE_WORKERS, currentPlayer);
    }

    protected int boardPlace(Board board, Position placePosition){
        Player currentPlayer = this.getCurrentPlayer();
        //for extra security
        if(!currentPlayer.isAnyWorkerNotPlaced()){
            return -1;
        }

        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            Optional<Integer> workerId = currentPlayer.addWorker(newWorker);
            if(!workerId.isPresent())
                throw new RuntimeException("Worker set in the board without reference from player");
            return workerId.get();
        }

        return -1;
    }

    /*@Override
    public boolean isValidPlace(Board board, Position placePosition) {
        Worker newWorker = new Worker();
        return board.setWorker(newWorker, placePosition);
    }*/
}


