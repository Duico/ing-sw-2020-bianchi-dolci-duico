package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.*;

import javax.swing.*;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

public class CliModel {
    private boolean isHost = false;
    public String nickname;
    private Player myPlayer;

    private Player currentPlayer;


    protected List<Player> players;

    protected boolean isTurnOK;
    private List<String> cards;


    private List<String> cardDeck;
    private Board board;
    private TurnPhase turnPhase;
    protected BoardPrinter bp;

    //advanced functionality
    Map<Position, Player> workersMap = new HashMap<Position, Player>();
    boolean workersMapOK = false;
    private String lastInfoMessage;

    /*
      All ask functions HAVE to be moved to another class

     */
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

    public void placeWorker(Position placePosition){
        Worker newWorker = new Worker();
        board.setWorker(newWorker, placePosition);
    }

    /*//TODO
    public boolean moveWorker(Position startPosition, Position destPosition){
        for(Player player: players){
            Integer workerId = player.getWorkerId(startPosition);
            if(workerId == null){
                System.out.println("Entro qui");
                return false;
            }

   //         return player.setWorkerPosition(workerId, destPosition);
             player.setWorkerPosition(workerId, destPosition);
        }
        return false;
    }*/

    public boolean moveWorker(Position startPosition, Position destPosition, Position pushPosition, Player playerEvent){
        if(pushPosition==null) {
            for (Player player : players) {
                Integer workerId = player.getWorkerId(startPosition);
                if (workerId == null) {

                    return false;
                }

                //         return player.setWorkerPosition(workerId, destPosition);
                player.setWorkerPosition(workerId, destPosition);
            }
        }else {
            for (Player player : players) {
                Integer workerId = player.getWorkerId(startPosition);
                if (workerId == null) {
                    System.out.println("Entro qui nel false worker ID");
                    return false;
                }
                player.setWorkerPosition(workerId, destPosition);

            }

            for (Player player : players) {
                if (!player.equalsUuid(playerEvent)) {
                    Integer workerId = player.getWorkerId(destPosition);
                    if (workerId == null) {
                        System.out.println("Entro qui nel false worker ID");
                        return false;
                    }
                    player.setWorkerPosition(workerId, pushPosition);

                }
            }
        }
        return false;
    }

    public void moveBoard(Position startPosition, Position destPosition, Position pushPosition){
        board.putWorkers(startPosition, destPosition, pushPosition);
    }


//    TODO
    public boolean buildWorker(Position workerPosition, Position buildPosition, boolean isDome){
        //board .build() is too complex for the Client

        board.build(workerPosition, buildPosition, isDome);
        return true;
    }
    public BoardPrinter createBoardPrinter(){
        //TODO
        //move generateWorkersMap
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

    private void testSetUp() {
        List<Player> playerList = new ArrayList<>();
        String[] playerNames = {"Pippo", "Pluto", "Topolino"};

//            Position[][] workersPositions = {
//                    {new Position(1, 1), new Position(2, 3)},
//                    {new Position(2, 0), new Position(3, 0)},
//                    {new Position(4, 4), new Position(3, 3)},
//            };
//            for(int i = 0; i<3; i++) {
//                ViewPlayer player = new ViewPlayer(playerNames[i], PlayerColor.values()[i]);
//                Worker worker1 = new Worker();
//                worker1.addMove(workersPositions[i][0]);
//                Worker worker2 = new Worker();
//                worker2.addMove(workersPositions[i][1]);
//                player.addWorker(worker1);
//                player.addWorker(worker2);
//                playerList.add(player);
//            }
            board = new Board(5,5);
            players = playerList;
//            return new BoardPrinter(newBoard, playerList);
//        }catch(PositionOutOfBoundsException e){
//
//        }


    }

    public void setPlayersIfNotSet(List<Player> players) {
        if(players != null) {
            this.players = players;
        }
    }

    public boolean setPlayerCard(Player player, String cardName) {
        Player playerReference;
        if((playerReference = getPlayerByUuid(player)) == null){
            return false;
        }else{
            playerReference.setCardName(cardName);
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
            if(player.equalsUuid(player))
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
}
