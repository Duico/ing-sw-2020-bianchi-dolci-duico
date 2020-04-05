package it.polimi.ingsw.model;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Manages a game from start to end, handles the creation and advancement of turns
 */
public class Game implements Serializable{
    private Turn turn;
    private Turn previousTurn;
    private ArrayList<Player> players = new ArrayList<>();
    boolean useCards = false;
    CardDeck cardDeck;

    public Game() {
        this(5, 5, 2);
    }

    /**
     * Create a new Game <b>without starting it</b>
     * @param width Width of the board
     * @param height Height of the board
     * @param numWorkers Number of workers for each player
     */
    public Game(int width, int height, int numWorkers) {
        Position.setSize(width, height);
        Player.setNumWorkers(numWorkers);
    }

    /**
     * Start this Game with chosen nicknames for the players
     * @param nicknames Array of the players nicknames, in display order
     * @param useCards  True if the game will be using cards
     * @return
     */
    public void startGame(ArrayList<String> nicknames, boolean useCards) {

        this.useCards = useCards;

        for (int n = 0; n < nicknames.size(); n++) {
            Player newPlayer = new Player(nicknames.get(n));
            players.add(newPlayer);
        }

        if (this.useCards) {
            //createCardDeck();  TO FIX
            //dealCards();
        } else {
            //assign default Card to each player
        }
        initTurn();
        //make an exceptional first turn that calls setWorker
    }

    private void createCardDeck() {
        try {
            this.cardDeck = new CardDeck();
        } catch (Exception e) {
            System.err.println("Error reading XML configuration file");
        }
    }

    public void dealCards() {
        ArrayList<Card> randomDeck = cardDeck.pickRandom(players.size());
        for (int i = 0; i < players.size(); i++) {
            Card randomCard = randomDeck.get(i);
            players.get(i).setCard(randomCard);
        }
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

    public void initTurn() {
        turn = new Turn( this.pickFirstPlayer() );
    }


    public void nextTurn() {
        //TODO
        previousTurn = turn;
        turn = new Turn( this.getNextPlayer() );
    }

    private Player pickFirstPlayer() {
            int rand = (int) Math.floor( Math.random() * (double) players.size() );
            Player randPlayer=players.get(rand);
            return randPlayer;
    }

    public Player getNextPlayer() {
        Player currentPlayer = turn.getCurrentPlayer();
        int index = players.indexOf(currentPlayer);
        if (index>0) {
            int size = players.size();
            return players.get( (index + 1) % size );
        }else {
            throw new RuntimeException("Current player was not found in Game player List");
        }
    }

    public Player getPlayer(int n) {
        return players.get(n);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        boolean playersEquals = true;
        //return useCards == game.useCards &&
                //Objects.equals(getTurn(), game.getTurn()) &&
                //Objects.equals(getPreviousTurn(), game.getPreviousTurn()) &&
                //Objects.equals(getPlayers(), game.getPlayers()) &&
                //Objects.equals(cardDeck, game.cardDeck);
        for(int i=0; i<getPlayers().size(); i++){
            playersEquals = playersEquals && getPlayer(i).getNickName().equals( game.getPlayer(i).getNickName() );
        }
        return useCards == game.useCards && playersEquals;

    }
}