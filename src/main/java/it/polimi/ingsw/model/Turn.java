package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    private final Player currentPlayer;
    private Worker currentWorker;
    private ArrayList<Position> moves;  //the initial position is setted by the game based the start position
    private ArrayList<Position> builds;  //the initial position is setted by the game based the start position
    private Turn previousTurn;
    private Board board;

    public Turn(Player currentPlayer){
        this.currentPlayer = currentPlayer;
    }

    public boolean move (Position startPosition, Position destinationPosition){
        boolean isRequiredToMove = currentWorker.getCard().getMoveStrategy().isRequiredToMove(moves, builds);
        boolean isAllowedToMove = currentWorker.getCard().getMoveStrategy().isAllowedToMove(moves, builds);
        BoardCell startCell = board.getBoardCell(startPosition);
        boolean validMove = board.validMove(startPosition,destinationPosition);

        if (isRequiredToMove == false && isAllowedToMove == false) return false;
        else if( validMove == true){
            board.setWorkerInTheCell(startPosition, destinationPosition);
            moves.add(destinationPosition);
            return true;
        }else{
            return false;
        }
    }

    public boolean build (Position startPosition, Position destinationPosition){
        return false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


}
