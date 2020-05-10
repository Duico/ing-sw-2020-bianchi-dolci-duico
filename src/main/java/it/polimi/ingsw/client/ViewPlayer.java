package it.polimi.ingsw.client;

import it.polimi.ingsw.model.FixedArray;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.WorkerPositionNotSetException;
import it.polimi.ingsw.view.View;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ViewPlayer {
    private final String nickName;
    private final UUID uuid;
    private String cardName; //FIX add final when tests are over
    private FixedArray<Worker> workers;
    private boolean isChallenger;
    private PlayerColor color; //TODO


    public ViewPlayer(String nickName, PlayerColor color) {
        this(nickName, color, 2);
    }
    public ViewPlayer(String nickName, PlayerColor color, int numWorkers) {
        this.uuid = UUID.randomUUID();
        this.nickName = nickName;
        this.isChallenger = false;
        this.color = color;
        workers = new FixedArray<>(numWorkers);
        //
        //this.initWorkers(numWorkers);
    }

    public String getCard() {
        return cardName;
    }

    public boolean setCard(String cardName) {
        if(this.cardName!=null)
            return false;
        else {
            this.cardName = cardName;
            return true;
        }
    }

//    public void initWorkers(int numWorkers){
//        workers = new FixedArray<>(numWorkers);
//    }

//    public int getNumMovesWorker(int worker) {
//        return workers.get(worker).getNumMoves();
//    }

//    public Operation getLastOperationWorker(int worker) {
//        return workers.get(worker).getLastOperation();
//    }

    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }

    public String getNickName() {
        return nickName;
    }

    public int getNumWorkers() {
        return workers.size();
    }


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

    public boolean isWorkerSet(int i) {
        return (workers.get(i) != null);
    }

    public int addWorker(Worker newWorker) {
        int workerId = workers.add(newWorker);
        return workerId;
    }

    public UUID getUuid() {
        return uuid;
    }
    public PlayerColor getColor(){
        return color;
    }

//    public void resetAllWorkers() {
//        for (int i = 0; i < getNumWorkers(); i++) {
//            if (!isWorkerSet(i))
//                continue;
//            Worker worker = workers.get(i);
//            worker.reset();
//        }
//    }
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

//    public void setIsChallenger(boolean isChallenger){
//        this.isChallenger=isChallenger;
//    }
//
//    public boolean isChallenger(){
//        return this.isChallenger;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewPlayer player = (ViewPlayer) o;
        boolean eq = Objects.equals(getUuid(), player.getUuid()) &&
                Objects.equals(getNickName(), player.getNickName()) &&
                Objects.equals(getCard(), player.getCard()) &&
                Objects.equals(workers, player.workers);
        return eq;
    }

//    @Override
//    public Player clone(){
//        try {
//            return (Player) super.clone();
//        }catch (CloneNotSupportedException e){
//            throw new RuntimeException("Clone not supported in Player");
//        }
//
//    }


    public int getWorkerId(Position workerPosition) {
        for(int i=0; i<workers.size(); i++){
            Worker worker = workers.get(i);
            try{
                if(worker!=null && worker.getCurrentPosition()==workerPosition){
                    return i;
                }
            }catch (WorkerPositionNotSetException e){

            }
        }
        return -1;
    }
}
