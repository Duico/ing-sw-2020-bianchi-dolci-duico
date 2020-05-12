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
    protected List<Player> players;
    protected Board board;
    protected TurnPhase turnPhase;
    protected BoardPrinter bp;
    private PrintStream out;
    private InputStream in;
    private Scanner stdin;

    public CliController(InputStream in, PrintStream out){
        this.in = in;
        board = new Board();
        players = new ArrayList<Player>();
        stdin = new Scanner(in);
        this.out = out;
        testSetUp();
    }

    public String askName(){
        out.print(CliText.ASK_NAME.toPrompt());
        String line = stdin.nextLine().trim();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            out.println(CliText.BAD_NAME);
            return null;
        }
        return line;
    }
    public Player askFirstPlayer(){
        StringBuilder playersLine = new StringBuilder();
        boolean isFirst = true;
        for(Player player: players){
            if(player.equalsUuid(myPlayer)){
                break;
            }
            playersLine.append(isFirst ? "" : ", " + player.getNickName());
            isFirst = false;
        }
        out.print(CliText.ASK_FIRSTPLAYER.toPrompt(playersLine.toString()));
        final String line = stdin.nextLine().trim();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            out.println(CliText.BAD_NAME);
            return null;
        }
        //check if line is in players
        List<Player> matchingPlayers;

        if((matchingPlayers = players.stream().filter( (p)-> (p.getNickName() == line)).collect(Collectors.toList()) ).size() == 0){
            out.println(CliText.BAD_PLAYERNAME.toString(line));
            return null;
        }
        return matchingPlayers.get(0);
    }
    public Integer askNumPlayers(){
        out.print(CliText.ASK_NUMPLAYERS.toPrompt());
        Integer line = stdin.nextInt();
        if(line<2 || line >3){
            out.println(CliText.BAD_NUMPLAYERS);
            return null;
        }
        return line;
    }
    protected List<String> askChallCards(List<String> cards){
        List<String> chosenCards = new ArrayList<>();
        String line;
        stdin = new Scanner(in);
        while( chosenCards.size() < getNumPlayers()){
            out.print(CliText.ASK_CHALLCARD.toPrompt(cards.toString()));
            while(!stdin.hasNextLine()) {

            }
            line = stdin.nextLine().trim();
            if (cards.contains(line)) {
                chosenCards.add(line);
                cards.remove(line);
            } else {
                out.print(CliText.BAD_CARD.toString(cards.toString()));
            }

        }
        return chosenCards;
    }
    public String askCard(List<String> chosenCards) {
        String line;
        out.print(CliText.ASK_CARD.toPrompt(chosenCards.toString()));
//        while(!stdin.hasNextLine()) {
//
//        }
        line = stdin.nextLine().trim();
        if (!chosenCards.contains(line)) {
            out.print(CliText.BAD_CARD.toPrompt(chosenCards.toString()));
        }
        return line;
    }

    public void printAll(){
        //if() turnPhase == NORMAL_TURN
        bp = new BoardPrinter(board, players);
        bp.printAll().printOut();
    }

    public boolean setTurnPhase(TurnPhase turnPhase){
        this.turnPhase = turnPhase;
        return true;
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

    public void setPlayers(List<Player> players) {
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

    private Player getPlayerByUuid(Player searchPlayer){
        for(Player player: players){
            if(player.equalsUuid(player))
                return player;
        }
        return null;
    }
}
