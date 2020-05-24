
package it.polimi.ingsw.client.gui;
import java.util.ArrayList;

public class GuiModel {
    private static GuiModel instance= null;

    private int numPlayers;
    private String username;
    private String chosenCard;
//    private Cell[][] board;
    private ArrayList<String> cards= new ArrayList<>();
    private ArrayList<String> players= new ArrayList<>();

    private GuiModel(){
//        initBoard();
    }

    public static GuiModel getInstance(){
        if(instance==null)
            instance=new GuiModel();
        return instance;
    }

//    private void initBoard(){
//        for(int i=0;i<5;i++)
//            for(int j=0;j<5;j++){
//                board[i][j]=new Cell();
//            }
//    }

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
