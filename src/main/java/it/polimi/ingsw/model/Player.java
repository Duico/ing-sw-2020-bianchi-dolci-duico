package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Player implements Serializable {
    private final String nickName;
    private Card card;
    private ArrayList<Worker> workers;
    static int numWorkers;

    public Player(String nickName){
        this.nickName = nickName;
        workers = new ArrayList<>();
        this.initWorkers(numWorkers);
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

    public int getNumBuildsWorker(int worker) {
        return workers.get(worker).getNumBuilds();
    }


    public String getNickName()
    {
        return nickName;
    }

    public static void setNumWorkers(int numWorkers){
        Player.numWorkers = numWorkers;
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


    public Position getCurrentPositionWorker(int currentWorkerId) {
        return workers.get(currentWorkerId).getCurrentPosition();
    }
}
