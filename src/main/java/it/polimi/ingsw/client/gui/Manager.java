
package it.polimi.ingsw.client.gui;
import java.util.ArrayList;

public class Manager {
    private static Manager instance= null;

    private int numPlayers;
    private String username;
    private String chosenCard;
    private ArrayList<String> cards= new ArrayList<>();
    private ArrayList<String> players= new ArrayList<>();

    private Manager(){}

    public static Manager getInstance(){
        if(instance==null)
            instance=new Manager();
        return instance;
    }

    public void setNumPlayers(int n){
        this.numPlayers=n;
    }

    public void setUsername(String name){
        this.username = name;
    }

    public void setChosenCard(String name){
        this.chosenCard=name;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public String getUsername(){
        return this.username;
    }

    public String getChosenCard(){
        return this.chosenCard;
    }

    public void addCard(String card){
        this.cards.add(card);
    }

    public String getCard(int i){
        return this.cards.get(i);
    }

    public void addPlayer(String name){
        this.players.add(name);
    }

    public String getPlayer(int i){
        return this.players.get(i);
    }
}
