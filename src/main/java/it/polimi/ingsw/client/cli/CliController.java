package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.*;

import javax.swing.*;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CliController {
    private boolean isHost = false;
    public String nickname;
    private Player myPlayer;

    private Player currentPlayer;


    protected List<Player> players;
    protected Board board;
    protected TurnPhase turnPhase;
    protected BoardPrinter bp;
    private PrintStream out;
    private InputStream in;
    private Scanner stdin;

    /*
      All ask functions HAVE to be moved to another class

     */
    public CliController(InputStream in, PrintStream out){
        this.in = in;
        board = new Board();
        players = new ArrayList<Player>();
        stdin = new Scanner(in);
        this.out = out;
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

    public void printAll(){
        synchronized (out) {
            //if() turnPhase == NORMAL_TURN
            bp = new BoardPrinter(board, players);
            out.print(" " + System.lineSeparator() + System.lineSeparator());
            bp.printAll().printOut(out);
        }
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


}
