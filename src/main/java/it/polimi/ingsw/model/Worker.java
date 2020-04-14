package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Single worker assigned to a player
 */
public class Worker implements Cloneable{

    private ArrayList<Position> moves; // initial positoin here
    private ArrayList<Position> builds; //initial position here
    private ArrayList<Operation> operations;

    public Worker(){
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
    }
    public void reset(){
        Position currentPosition = getCurrentPosition();
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
        //needed for getCurrentPosition
        this.moves.add(currentPosition);
    }

    public int getNumMoves(){
        return moves.size()-1;
    }

    public int getNumBuilds(){
        return builds.size();
    }

    public Position getCurrentPosition(){
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

    public void deleteLastMove(){
        moves.remove(moves.size()-1);
    }

    public void deleteLastBuild(){
        builds.remove(builds.size()-1);
    }

    @Override
    public Worker clone() throws CloneNotSupportedException {
        Worker newWorker = (Worker) super.clone();
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

        return newWorker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(getMoves(), worker.getMoves()) &&
                Objects.equals(getBuilds(), worker.getBuilds()) &&
                Objects.equals(operations, worker.operations);
    }

    public Operation getLastOperation() {
            return operations.get(operations.size()-1);
    }
}
