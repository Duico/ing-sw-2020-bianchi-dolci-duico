package it.polimi.ingsw.model;

import java.util.ArrayList;

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
    }

//    public void setCurrentPosition(Position position) {
//        moves.add(position);
//    }

    @Override
    public Worker clone() throws CloneNotSupportedException {
        Worker newWorker = (Worker) super.clone();
        //clone moves
        ArrayList<Position> newMoves = new ArrayList<>();
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
        newWorker.operations = newOperations;

        return newWorker;
    }
}
