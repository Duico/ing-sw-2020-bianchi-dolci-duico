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

    /**
     * reset current position, movement e building lists
     */
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

    }


    public Operation getLastOperation() {
        if(operations.size()>0)
            return operations.get(operations.size()-1);
        else
            return null;
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

}
