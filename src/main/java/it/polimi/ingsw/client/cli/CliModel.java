package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Optional;
import java.util.*;

/**
 * Class which implement a thin model for the Cli
 */

public class CliModel {
    private boolean isHost = false;
    public String nickname;
    private Player myPlayer;
    private Player currentPlayer;
    protected List<Player> players;
    private boolean endGame = false;
    protected boolean isTurnOK;
    private List<String> cards;
    private List<String> cardDeck;
    private Board board;
    private TurnPhase turnPhase;
    protected BoardPrinter bp;

    Map<Position, Player> workersMap = new HashMap<Position, Player>();
    boolean workersMapOK = false;
    private String lastInfoMessage;


    public CliModel(){
        board = new Board();
        players = new ArrayList<Player>();
    }

    public boolean isMyTurn(){
       return getCurrentPlayer().equalsUuid(getMyPlayer());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setBoard(Board board){
        this.board = board;
    }

    /**
     * Function that after the arrive of an event with the list of the players updated from the server,
     * update in local the player
     * @param player
     */
    public void updatePlayer(Player player) {
        if(players == null){
            return;
        }
        for (int i = 0; i < players.size(); ++i) {
            Player oldPlayer = players.get(i);
            if (oldPlayer.equalsUuid(player)) {
                players.set(i, player);
                break;
            }
        }
    }

    /**
     * Function that places a worker in the local board
     * @param placePosition
     */
    public void placeWorker(Position placePosition){
        Worker newWorker = new Worker();
        board.setWorker(newWorker, placePosition);
    }

    /**
     * Function that is called after a player defeat from the game
     * This function remove the player from the local list of the players
     * @param player
     */
    public void removePlayer(Player player){
        for(int i=0;i<players.size();i++){
            if(player.equalsUuid(players.get(i))) {
                players.remove(players.get(i));
                break;
            }
        }
    }

    /**
     * This function move the worker and update the worker position.
     * It is called after the arrive of the event that confirm the movement on the server.
     * @param startPosition
     * @param destPosition
     * @param pushPosition
     * @param playerEvent
     * @return
     */
    public boolean moveWorker(Position startPosition, Position destPosition, Position pushPosition, Player playerEvent){
        for (Player player : players) {
            Optional<Integer> workerId = player.getWorkerId(startPosition);
            if (!workerId.isPresent() || workerId.get() == null) {
                //return false;
            }else{
                player.setWorkerPosition(workerId.get(), destPosition);
            }


        }
        if(pushPosition!=null) {
            for (Player player : players) {
                if (!player.equalsUuid(playerEvent)) {
                    Optional<Integer> workerId = player.getWorkerId(destPosition);
                    if (!workerId.isPresent() || workerId.get() == null) {

                    }else{
                        player.setWorkerPosition(workerId.get(), pushPosition);
                    }

                }
            }
        }
        return false;
    }

    /**
     * This function update the local board after a movement
     * It is called after the arrive of the event that confirm the movement on the server.
     * @param startPosition
     * @param destPosition
     * @param pushPosition
     */
    public void moveBoard(Position startPosition, Position destPosition, Position pushPosition){
        board.putWorkers(startPosition, destPosition, pushPosition);
    }

    /**
     * This function update the local board after a building
     * It is called after the arrive of the event that confirm the building on the server.
     * @param workerPosition
     * @param buildPosition
     * @param isDome
     * @return true
     */
    public boolean buildWorker(Position workerPosition, Position buildPosition, boolean isDome){
        board.build(workerPosition, buildPosition, isDome);
        return true;
    }

    public BoardPrinter createBoardPrinter(){
        generateWorkersMap();
        return bp = new BoardPrinter(board, players, workersMap);
    }

    public List<String> getCardDeck() {
        return cardDeck;
    }

    public void setCardDeck(List<String> cardDeck) {
        this.cardDeck = cardDeck;
    }

    public List<String> getCards() {
        return cards;
    }

    public void setCards(List<String> cards) {
        this.cards = cards;
    }

    public boolean setTurnPhase(TurnPhase turnPhase){
        this.turnPhase = turnPhase;
        return true;
    }
    public TurnPhase getTurnPhase() {
        return turnPhase;
    }

    public Player getMyPlayer() {
        return myPlayer;
    }

    public void setMyPlayer(Player myPlayer){
        this.myPlayer = myPlayer;
    }

    public int getNumPlayers(){
        return players.size();
    }

    public Integer getBoardWidth(){
        if(board == null) return null;
        return board.getWidth();
    }

    public Integer getBoardHeight(){
        if(board == null) return null;
        return board.getHeight();
    }


    public void setPlayersIfNotSet(List<Player> players) {
        if(players != null) {
            this.players = players;
        }
    }

    public boolean setPlayerCard(Player player, Card card) {
        Player playerReference;
        if((playerReference = getPlayerByUuid(player)) == null){
            return false;
        }else{

            playerReference.setCard(card);
            return true;
        }
    }

    public String getPlayerCard(Player player){
        Player playerReference = getPlayerByUuid(player);
        if(playerReference != null){
            return playerReference.getCardName();
        }else{
            return null;
        }
    }

    private Player getPlayerByUuid(Player searchPlayer){
        for(Player player: players){
            if(player.equalsUuid(searchPlayer))
                return player;
        }
        return null;
    }

    boolean generateWorkersMap(){
        workersMap.clear();
        int count = 0;
        for(Player player: players){
            for(int i = 0; i<player.getNumWorkers(); i++) {
                Position pos = player.getWorkerPosition(i);
                if(pos!=null){
                    workersMap.put(pos, player);
                    count++;
                }
            }
        }
        if(count>0) return true;
        else return false;
    }

    public void setLastInfoMessage(String infoMessage) {
        this.lastInfoMessage = infoMessage;
    }
    public String getLastInfoMessage(){
        return lastInfoMessage;
    }

    public void setEndGame(){
        this.endGame=true;
    }

    public boolean getEndGame(){
        return this.endGame;
    }
}
