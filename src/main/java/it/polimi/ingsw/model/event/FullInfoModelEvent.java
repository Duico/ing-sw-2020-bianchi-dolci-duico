package it.polimi.ingsw.model.event;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.CardDeck;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;
import java.util.List;

public class FullInfoModelEvent extends ModelEvent {
    private Board board;
    private List<Player> players;
    private CardDeck cardDeck;
    public FullInfoModelEvent(Player currentPlayer, List<Player> players, Board board, CardDeck cardDeck) {
        super(currentPlayer);
        this.players = players;
        this.board = board;
        this.cardDeck = cardDeck;
    }
}
