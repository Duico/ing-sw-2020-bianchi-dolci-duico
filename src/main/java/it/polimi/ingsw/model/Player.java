package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    private final String nickName;
    private Card card; //FIX add final when tests are over
    private FixedArray<Worker> workers;
    private final int workersToPlace;


    public Player(String nickName, int numWorkers){
        this.nickName = nickName;
        workers = new FixedArray<>(numWorkers);
        workersToPlace = numWorkers;
        //this.initWorkers(numWorkers);
    }

    private void initWorkers(int numWorkers){
        for(int i=0; i < numWorkers; i++){
            workers.add(new Worker());
        }
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) { // to delete when we finish the test

        this.card = card;
    }

    public int getNumMovesWorker(int worker) {
        return workers.get(worker).getNumMoves();
    }

    public Operation getLastOperationWorker(int worker){
        return workers.get(worker).getLastOperation();
    }

    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }

    public String getNickName()
    {
        return nickName;
    }

    public int getNumWorkers(){
        return workers.size();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(getNickName(), player.getNickName()) &&
                Objects.equals(getCard(), player.getCard()) &&
                Objects.equals(workers, player.workers);
    }


    public Position getWorkerCurrentPosition(int currentWorkerId) {
        return workers.get(currentWorkerId).getCurrentPosition();
    }

    public boolean addWorker(Worker newWorker){
        if(!canPlace()){
            return false;
        }else{
            workers.add(newWorker);
            return true;
        }
    }

    public boolean canPlace() {

        return (workersToPlace>0);
    }
}
