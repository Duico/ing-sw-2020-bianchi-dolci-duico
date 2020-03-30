package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Game {
    private Turn turn;
    private Turn previousTurn;
    private ArrayList<Player> players;
    final boolean useCards;

    public Game(ArrayList<String> nicknames, boolean useCards){
        this.useCards = useCards;
        //FIX numWorkers hard-coded
        int numWorkers = 2;
        for( int n = 0; n<nicknames.size(); n++ ){
            players.add( new Player(this, nicknames.get(n), numWorkers) );
        }
        if(this.useCards){
            dealCards();
        }
    }

    public void dealCards(){

    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Turn getPreviousTurn() {
        return previousTurn;
    }

    public void nextTurn(){
        //TODO
        //save current turn in previousTurn and make a new playable turn
    }

    public Player getPlayer(int n){
        return players.get(n);
    }
}