package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Game {
    private Turn turn;
    private Turn previousTurn;
    private ArrayList<Player> players = new ArrayList<>();
    boolean useCards = false;
    CardDeck cardDeck;
    private final String persistencyFilename = "./game.ser";

    public Game() {
        this(5, 5, 2);
    }

    /**
     * Create
     *
     * @param width
     * @param height
     * @param numWorkers
     */
    public Game(int width, int height, int numWorkers) {
        Position.setSize(width, height);
    }

    /**
     * Create a new Game with chosen nicknames for the players
     *
     * @param nicknames Array of the players nicknames, in display order
     * @param useCards  True if the game will be using cards
     * @return
     */
    public static Game createGame(ArrayList<String> nicknames, boolean useCards) {
        Game game = new Game();

        game.useCards = useCards;
        //FIX numWorkers hard-coded
        int numWorkers = 2;

        for (int n = 0; n < nicknames.size(); n++) {
            Player newPlayer = new Player(nicknames.get(n));
            game.players.add(newPlayer);
        }

        if (game.useCards) {
            //createCardDeck();  TO FIX
            //dealCards();
        } else {
            //assign default Card to each player
        }
        return game;
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

    public void nextTurn() {
        //TODO
        //save current turn in previousTurn and make a new playable turn
    }

    public Player getPlayer(int n) {
        return players.get(n);
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

}