package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Single player of the game
 */

public class Player implements Serializable, Cloneable {
    private final String nickName;
    private final UUID uuid;
    private Card card;
    private FixedArray<Worker> workers;
    private boolean isChallenger;
    private PlayerColor color;


    /**
     *
     * @param nickName nickname of the player
     */
    public Player(String nickName) {
        this.uuid = UUID.randomUUID();
        this.nickName = nickName;
        this.isChallenger = false;//

    }



    public void setColor(PlayerColor color) {
        this.color = color;
    }


    public Card getCard() {
        return card;
    }

    public boolean setCard(Card card) {
        if(this.card!=null)
            return false;
        else {
            this.card = card;
            return true;
        }
    }

    public String getCardName(){
        if(card!=null){
            return card.getName();
        }else{

            return null;
        }
    }

    /**
     * Create the array that contains the workers of the player
     * @param numWorkers number of workers
     */
    public void initWorkers(int numWorkers){
        workers = new FixedArray<>(numWorkers);
    }

    /**
     * return the number of movements of a worker
     * @param worker id of the worker
     * @return number of movements
     */
    public int getNumMovesWorker(int worker) {
        return workers.get(worker).getNumMoves();
    }

    /**
     * return the last operation of a worker
     * @param worker id of a worker
     * @return last operation
     */
    public Operation getLastOperationWorker(int worker) {
        return workers.get(worker).getLastOperation();
    }

    /**
     * return the number of buildings of a worker
     * @param worker id of the worker
     * @return number of buildings
     */
    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }

    public String getNickName() {
        return nickName;
    }

    public int getNumWorkers() {
        return workers.size();
    }

    /**
     *checks if worker related to workerId exists and return his position in the Board
     * @param workerId related one of player's workers
     * @return position where worker is located
     */
    public Position getWorkerPosition(int workerId) {
        Worker worker = workers.get(workerId);
        if (worker == null) {
            return null;
        }
        try {
            return worker.getCurrentPosition();
        }catch(WorkerPositionNotSetException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * return if a worker is set in the array of workers
     * @param i index of the array
     * @return true if the worker is set, false in the other case
     */
    public boolean isWorkerSet(int i) {
        return (workers.get(i) != null);
    }

    /**
     * add a worker in the array of player workers
     * @param newWorker worker to add
     * @return id of the worker
     */
    public Optional<Integer> addWorker(Worker newWorker) {
        return workers.add(newWorker);
    }

    /**
     *
     * @return true if there are any worker not placed, false in the other case
     */
    public boolean isAnyWorkerNotPlaced() {
        return (workersToPlace() > 0);
    }

    private int workersToPlace() {
        return workers.nullCount();
    }

    public UUID getUuid() {
        return uuid;
    }
    public PlayerColor getColor(){
        return color;
    }

    /**
     * reset all the workers contained in the array of player workers
     */
    public void resetAllWorkers() {
        for (int i = 0; i < getNumWorkers(); i++) {
            if (!isWorkerSet(i))
                continue;
            Worker worker = workers.get(i);
            worker.reset();
        }
    }

    /**
     * checks if there's a worker located in destination position
     * @param destinationPosition position to check
     * @return true if there is a worker located in destination position
     */
    public boolean isOwnWorkerInPosition(Position destinationPosition) {

        for (int i = 0; i < getNumWorkers(); i++) {
            try {
                if (workers.get(i).getCurrentPosition().equals(destinationPosition))
                    return true;
            }catch(WorkerPositionNotSetException e){

            }
        }
        return false;
    }

    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }

    public boolean isChallenger(){
        return this.isChallenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        boolean eq = Objects.equals(getUuid(), player.getUuid()) &&
                Objects.equals(getNickName(), player.getNickName()) &&
                Objects.equals(getCard(), player.getCard()) &&
                Objects.equals(workers, player.workers);
        return eq;
    }

    /**
     *
     * @param player
     * @return true if player has the same Uuid, false in the other case
     */
    public boolean equalsUuid(Player player){
        if (this == player) return true;
        return Objects.equals(getUuid(), player.getUuid());
    }

    @Override
    public Player clone(){
        try {
            return (Player) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException("Clone not supported in Player");
        }

    }


    /**
     *
     * @param workerPosition position of the worker
     * @return Integer id of the worker, Optional.Empty if no worker is contained in workerPosition
     */
    public Optional<Integer> getWorkerId(Position workerPosition) {
        for(int i=0; i<workers.size(); i++){
            Worker worker = workers.get(i);
            try{
                if(worker!=null && worker.getCurrentPosition().equals(workerPosition)){
                    return Optional.of(i);
                }
            }catch (WorkerPositionNotSetException e){
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    /**
     * sets position to worker having Id equals to parameter
     * @param i worker id
     * @param destPosition position to set for worker
     * @return true if position correctly set
     */
    public boolean setWorkerPosition(int i, Position destPosition) {
        try{
            workers.get(i).addMove(destPosition);
            return true;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }
}
