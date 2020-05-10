package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ViewPlayer;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CliController {
    private boolean isHost = false;
    public String nickname;
    List<ViewPlayer> players;
    Board board;
    TurnPhase turnPhase;
    BoardPrinter bp;
    PrintStream out;
    Scanner stdin;

    public CliController(Scanner in, PrintStream out){
        board = new Board();
        players = new ArrayList<>();
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

    public void printAll(){
        //if() turnPhase == NORMAL_TURN
        bp = new BoardPrinter(board, players);
        bp.printAll().printOut();
    }



    private void testSetUp() {
        List<ViewPlayer> playerList = new ArrayList<>();
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
