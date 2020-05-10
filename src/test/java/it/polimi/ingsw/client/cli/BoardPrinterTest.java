package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ViewPlayer;
import it.polimi.ingsw.model.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

class BoardPrinterTest {
    private BoardPrinter boardPrinter;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream outPrintStream = new PrintStream(outContent);

    @BeforeEach
    void setUp() throws Exception{
        outContent.reset();

        List<ViewPlayer> playerList = new ArrayList<>();
        String[] playerNames = {"Pippo", "Pluto", "Topolino"};
        Position[][] workersPositions = {
                {new Position(1,1), new Position(2,3)},
                {new Position(2,0), new Position(3,0)},
                {new Position(4,4), new Position(3,3)},
        };
        for(int i = 0; i<3; i++) {
            ViewPlayer player = new ViewPlayer(playerNames[i], PlayerColor.values()[i]);
            Worker worker1 = new Worker();
            worker1.addMove(workersPositions[i][0]);
            Worker worker2 = new Worker();
            worker2.addMove(workersPositions[i][1]);
            player.addWorker(worker1);
            player.addWorker(worker2);
            playerList.add(player);
        }
        Board newBoard = new Board();
        boardPrinter = new BoardPrinter(newBoard, playerList);

    }

    @Test
    void displayCell() throws Exception{
        boardPrinter.displayCell(new Position(2,3)).printOut(outPrintStream);
        assertEquals(
                "|         |\n" +
                "|    \u001B[1;37mW\u001B[0m    |\n" +
                "|    0    |\n" +
                "|_________|\n",
                outContent.toString());
    }
    @Test
    void printBoard() throws Exception{
        boardPrinter.printBoard().printOut(System.out);
    }
}