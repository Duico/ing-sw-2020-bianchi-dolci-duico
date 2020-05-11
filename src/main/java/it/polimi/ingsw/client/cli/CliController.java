package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliController {
    private boolean isHost = false;
    public String nickname;
    private Player myPlayer;
    protected List<Player> players;
    protected Board board;
    protected TurnPhase turnPhase;
    protected BoardPrinter bp;
    private PrintStream out;
    private Scanner stdin;

    public CliController(Scanner in, PrintStream out){
        board = new Board();
        players = new ArrayList<Player>();
        stdin = in;
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
        while( chosenCards.size() < getNumPlayers()){
            out.print(CliText.ASK_CHALLCARD.toPrompt(cards.toString()));
            line = stdin.nextLine().trim();
            if(cards.contains(line)){
                chosenCards.add(line);
                cards.remove(line);
            }
        }
        return chosenCards;
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
}
