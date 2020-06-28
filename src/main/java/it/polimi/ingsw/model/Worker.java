package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for a single worker assigned to a player
 */
public class Worker implements Cloneable, Serializable {

    private ArrayList<Position> moves; // initial position here
    private ArrayList<Position> builds;
    private ArrayList<Operation> operations;

    public Worker(){
        this.moves = new ArrayList<Position>();
        this.builds = new ArrayList<Position>();
        this.operations = new ArrayList<Operation>();
    }

    /**
     * reset current position, movement and building lists
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

    /**
     * @return position where the worker is currently located at
     * @throws WorkerPositionNotSetException thrown when current worker position is not set
     */
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


    /**
     * Add a movement in the list of movements
     * @param position position where the worker has moved
     */
    public void addMove(Position position) {
        moves.add(position);
        operations.add(Operation.MOVE);

    }

    /**
     * Add a building in the list of buildings
     * @param position the worker has built
     */
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


    /**
     * @return last operation done by the worker
     */
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
