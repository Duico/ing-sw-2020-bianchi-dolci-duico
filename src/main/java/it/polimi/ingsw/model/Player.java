package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
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


    public Worker getWorker(int i){
        return workers.get(i);
    }

    public String getNickName()
    {
        return nickName;
    }

    public static void setNumWorkers(int numWorkers){
        Player.numWorkers = numWorkers;
    }
}
