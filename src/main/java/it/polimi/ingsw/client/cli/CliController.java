package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ViewPlayer;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.TurnPhase;

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
    }

    public String askName(){
        out.print(CliText.ASK_NAME.toPrompt());
        String line = stdin.nextLine().trim();
        while(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            out.println(CliText.BAD_NAME);
            line = stdin.nextLine().trim();
        }
        return line;
    }

    private void printAll(){
        //if() turnPhase == NORMAL_TURN
        bp.printAll();
    }


}
