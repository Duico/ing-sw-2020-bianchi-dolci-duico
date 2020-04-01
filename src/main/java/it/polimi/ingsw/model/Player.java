package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Player {
    private final String nickName;
    private Card card;
    private Game game;
    private ArrayList<Worker> workers;
    static int numWorkers;

    private Player(Game game, String nickName){
        this.game = game;
        this.nickName = nickName;
        workers = new ArrayList<>();
    }

    public static Player createPlayer(Game game, String nickname){
        Player player = new Player(game, nickname);
        player.initWorkers(numWorkers);
        return player;
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

    public Game getGame() {

        return game;
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
