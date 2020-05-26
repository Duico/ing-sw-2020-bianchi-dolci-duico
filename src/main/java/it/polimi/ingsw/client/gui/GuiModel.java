
package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GuiModel {



    private static GuiModel instance= null;

    private int numPlayers;
    private String currentUsername;
    private String currentCard;
//    private Cell[][] board;
    private List<String> cards= new ArrayList<>();
    private List<String> cardDeck= new ArrayList<>();
//    private List<String> playerNames = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private GuiModel( ){

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

//    public void setPlayerNames(List<Player> names){
//        for(Player player:names){
//            this.playerNames.add(player.getNickName());
//        }
//
//        this.setNumPlayers(names.size());
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

    public void setCards(List<String> cards){
        this.cards=cards;
    }

    public List<String> getCards(){
        return this.cards;
    }

    public void setNumPlayers(int n){
        this.numPlayers=n;
    }

    public void setCurrentUsername(String name){
        this.currentUsername = name;
    }

    public void setCurrentCard(String name){
        this.currentCard =name;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public String getCurrentUsername(){
        return this.currentUsername;
    }

    public String getCurrentCard(){
        return this.currentCard;
    }

    public void addCard(String card){
        this.cards.add(card);
    }

    public String getCard(int i){
        return this.cards.get(i);
    }

//    public void addPlayer(String name){
//        this.playerNames.add(name);
//    }

//    public String getPlayer(int i){
//        return this.playerNames.get(i);
//    }
}
