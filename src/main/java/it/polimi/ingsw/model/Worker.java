package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Single worker assigned to a player
 */
public class Worker implements Cloneable, Serializable {

    private ArrayList<Position> moves; // initial positoin here
    private ArrayList<Position> builds; //initial position here
    private ArrayList<Operation> operations;

    public Worker(){
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
    }
    public void reset(){
        Position currentPosition = null;
        try{
            currentPosition = getCurrentPosition();
        }catch(WorkerPositionNotSetException e){
            e.printStackTrace();
        }
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
        if(currentPosition!=null) {
            //needed for getCurrentPosition
            this.moves.add(currentPosition);
        }
    }

    public int getNumMoves(){
        return moves.size()-1;
    }

    public int getNumBuilds(){
        return builds.size();
    }

    public Position getCurrentPosition() throws WorkerPositionNotSetException{
        if(this.moves.size() == 0){
            throw new WorkerPositionNotSetException();
        }
        return this.moves.get( this.moves.size()-1 );
    }

    public Position getFirstMove(){ return this.moves.get(0); }

    public Position getFirstBuild(){ return this.builds.get(0); }

    public ArrayList<Position> getMoves() {
        return moves;
    }

    public ArrayList<Position> getBuilds() {
        return builds;
    }

//    public List<Operation> play() {
//        MoveStrategy moveStrategy = card.getMoveStrategy();
//        BuildStrategy buildStrategy = card.getBuildStrategy();
//        boolean isRequiredToMove = moveStrategy.isRequiredToMove(this);
//        boolean isRequiredToBuild = buildStrategy.isRequiredToBuild(this);
//        boolean isAllowedToMove = moveStrategy.isAllowedToMove(this);
//        boolean isAllowedToBuild = buildStrategy.isAllowedToBuild(this);
//
//
//        while (isRequiredToMove || isRequiredToBuild) {
//            //make a choice between move and build
//
//            if (isAllowedToMove && isAllowedToBuild) {
//                //can use both cursors
//
//            } else if (isAllowedToMove) {
//                //change cursor to move
//
//            } else if (isAllowedToBuild) {
//                //change cursor to build
//
//            } else {
//                //undo or lose game
//            }
//
//        }
//
//        while (isAllowedToMove || isAllowedToBuild) {
//            //user is able to skip to next turn
//
//            if (isAllowedToMove && isAllowedToBuild) {
//                //can use both cursors
//            } else if (isAllowedToMove) {
//                //change cursor to move
//
//            } else if (isAllowedToBuild) {
//                //change cursor to build
//
//            } else {
//                //next turn
//            }
//        }
//
//
//
//    }


    public void addMove(Position position) {
        moves.add(position);
        operations.add(Operation.MOVE);

    }

    public void addBuild(Position position){
        builds.add(position);
        operations.add(Operation.BUILD);
    }


    @Override
    public Worker clone(){
        Worker newWorker;
        try {
            newWorker = (Worker) super.clone();
            newWorker.moves = (ArrayList<Position>) this.moves.clone();
            newWorker.builds = (ArrayList<Position>) this.builds.clone();
            newWorker.operations = (ArrayList<Operation>) this.operations.clone();

        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported on BoardCell");
        }
        return newWorker;
        //clone moves. Position operation are immutabile
        /*ArrayList<Position> newMoves = new ArrayList<>();
        for(Position move: this.moves){
            newMoves.add(move);
        }
        newWorker.moves = newMoves;
        //clone builds
        ArrayList<Position> newBuilds = new ArrayList<>();
        for(Position build: this.builds){
            newBuilds.add(build);
        }
        newWorker.builds = newBuilds;
        //clone operations
        ArrayList<Operation> newOperations = new ArrayList<>();
        for(Operation operation : this.operations){
            newOperations.add(operation);
        }
        newWorker.operations = newOperations;*/
    }


    public Operation getLastOperation() {
            return operations.get(operations.size()-1);
    }
}
