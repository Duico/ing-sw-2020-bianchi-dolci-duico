package it.polimi.ingsw.model;

import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Player implements Serializable {
    private final String nickName;
    private final UUID uuid;
    private Card card; //FIX add final when tests are over
    private FixedArray<Worker> workers;
    private boolean isChallenger;


    public Player(String nickName) {
        this.uuid = UUID.randomUUID();
        this.nickName = nickName;
        this.isChallenger = false;
        //
        //this.initWorkers(numWorkers);
    }

//    private void initWorkers(int numWorkers){
//        for(int i=0; i < numWorkers; i++){
//            workers.add(new Worker());
//        }
//    }

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

    public void initWorkers(int numWorkers){
        workers = new FixedArray<>(numWorkers);
    }

    public int getNumMovesWorker(int worker) {
        return workers.get(worker).getNumMoves();
    }

    public Operation getLastOperationWorker(int worker) {
        return workers.get(worker).getLastOperation();
    }

    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }

    public String getNickName() {
        return nickName;
    }

    public int getNumWorkers() {
        return workers.size();
    }


    public Position getWorkerCurrentPosition(int currentWorkerId) {
        Worker worker = workers.get(currentWorkerId);
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

    public boolean isWorkerSet(int i) {
        return (workers.get(i) != null);
    }

    public int addWorker(Worker newWorker) {
        int workerId = workers.add(newWorker);
        return workerId;
    }

    public boolean isAnyWorkerNotPlaced() {
        return (workersToPlace() > 0);
    }

    private int workersToPlace() {
        int count = 0;
        for (int i = workers.size() - 1; i >= 0; i--) {
            if (workers.get(i) == null) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public UUID getUuid() {
        return uuid;
    }


    public void resetAllWorkers() {
        for (int i = 0; i < getNumWorkers(); i++) {
            if (!isWorkerSet(i))
                continue;
            Worker worker = workers.get(i);
            worker.reset();
        }
    }
    public boolean isOwnWorkerInPosition(Position destinationPosition) {
        boolean isOwnWorker = false;
        for (int i = 0; i < getNumWorkers(); i++) {
            if (getWorkerCurrentPosition(i).equals(destinationPosition))
                isOwnWorker = true;
        }
        return true;
    }

    public void setIsChallenger(boolean isChallenger){
        this.isChallenger=isChallenger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getUuid(), player.getUuid()) &&
                Objects.equals(getNickName(), player.getNickName()) &&
                Objects.equals(getCard(), player.getCard()) &&
                Objects.equals(workers, player.workers);
    }

}
