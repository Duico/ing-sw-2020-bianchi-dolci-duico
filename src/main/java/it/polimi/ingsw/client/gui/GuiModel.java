
package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GuiModel {



    private static GuiModel instance= null;

    private int numPlayers;
    private String username;
    private String chosenCard;
//    private Cell[][] board;
    private List<String> cards= new ArrayList<>();
    private List<String> cardDeck= new ArrayList<>();
    private List<String> playerNames = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    public GuiModel( ){

    }

    public static GuiModel getInstance(){
        if(instance==null)
            instance=new GuiModel();
        return instance;
    }


    public void setPlayers(List<Player> players) {
        this.players = players;
        this.setNumPlayers(players.size());
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayerNames(List<Player> names){
        for(Player player:names){
            this.playerNames.add(player.getNickName());
        }

        this.setNumPlayers(names.size());
    }

    public List<String> getPlayerNames(){
        return this.playerNames;
    }

//    private void initBoard(){
//        for(int i=0;i<5;i++)
//            for(int j=0;j<5;j++){
//                board[i][j]=new Cell();
//            }
//    }

    public void setCardDeck(List<String> card){
        for(String newCard:card){
            if(!this.cardDeck.contains(newCard))
                cardDeck.add(newCard);
        }
    }

    public List<String> getCardDeck(){
        return this.cardDeck;
    }

    public void setCards(List<String> card){
        this.cards.addAll(card);
    }

    public List<String> getCards(){
        return this.cards;
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
        this.playerNames.add(name);
    }

    public String getPlayer(int i){
        return this.playerNames.get(i);
    }
}
