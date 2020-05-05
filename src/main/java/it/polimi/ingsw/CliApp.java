package it.polimi.ingsw;

import it.polimi.ingsw.client.ViewPlayer;
import it.polimi.ingsw.client.cli.BoardPrinter;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.PlayerColor;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.util.ArrayList;
import java.util.List;

public class CliApp {
    public static void main( String[] args )
    {
       final BoardPrinter cp = testSetUp();
       cp.clear();
       cp.cellWidth = 1;
       cp.printBoard();


    }

    static BoardPrinter testSetUp() {
        List<ViewPlayer> playerList = new ArrayList<>();
        String[] playerNames = {"Pippo", "Pluto", "Topolino"};
        try {
            Position[][] workersPositions = {
                    {new Position(1, 1), new Position(2, 3)},
                    {new Position(2, 0), new Position(3, 0)},
                    {new Position(4, 4), new Position(3, 3)},
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
            return new BoardPrinter(newBoard, playerList);
        }catch(PositionOutOfBoundsException e){

        }
        return null;
    }
}
