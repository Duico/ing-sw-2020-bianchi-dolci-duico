package it.polimi.ingsw.model;

public class PlaceWorkersTurn extends Turn {

    public PlaceWorkersTurn(Player currentPlayer) {
        super(TurnPhase.PLACE_WORKERS, currentPlayer);
    }

    protected Optional<Integer> boardPlace(Board board, Position placePosition){
        Player currentPlayer = this.getCurrentPlayer();
        //for extra security
        if(!currentPlayer.isAnyWorkerNotPlaced()){
            return Optional.empty();
        }

        Worker newWorker = new Worker();
        if(board.setWorker(newWorker, placePosition)) {
            Optional<Integer> workerId = currentPlayer.addWorker(newWorker);
            System.out.println(workerId.get());
            if(!workerId.isPresent())
                throw new RuntimeException("Worker set in the board without reference from player");
            return workerId;
        }

        return Optional.empty();
    }

    /*@Override
    public boolean isValidPlace(Board board, Position placePosition) {
        Worker newWorker = new Worker();
        return board.setWorker(newWorker, placePosition);
    }*/
}


