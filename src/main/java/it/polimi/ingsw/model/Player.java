package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nickName;
    private Card card;
    private Game game;
    private ArrayList<Worker> workers;

    public Player(Game game, String nickName, int numWorkers){
        this.game = game;
        this.nickName = nickName;
        for(int i=0; i < numWorkers; i++)
            workers.add(new Worker(this));

    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) { // to delete when we finish the test

        this.card = card;
    }

    public Game getGame() {

        return game;
    }

}
