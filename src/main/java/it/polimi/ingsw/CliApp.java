package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.view.ViewEventListener;


public class CliApp {
    public static void main( String[] args )
    {
        CliModelEventVisitor cliVisitor = new CliModelEventVisitor();
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345, cliVisitor, cliVisitor, cliVisitor);
        cliVisitor.addEventListener(ViewEventListener.class, clientConnection);
        cliVisitor.addEventListener(SignUpListener.class, clientConnection);

        Thread connectionThread = new Thread(clientConnection);
        connectionThread.start();
        Thread thread2 = new Thread(cliVisitor);
        thread2.start();


        /*
        while(scanner.hasNextLine()){
            String input = scanner.nextLine();
            if(input.indexOf("+") == 0){
                cp.setCellWidth(cp.getCellWidth()+1);
            }else if(input.indexOf("-") == 0){
                cp.setCellWidth(cp.getCellWidth()-1);
            }
            cp.clear();
            printBoard = cp.printAll();
            printBoard.printOut();
            //  prompt for the user's name
            System.out.println("");
            System.out.println("Enter command: ");
            System.out.print("> ");
            //System.out.println(printBoard.getWidth());
        }
        */
    }
//
//    static BoardPrinter testSetUp() {
//        List<ViewPlayer> playerList = new ArrayList<>();
//        String[] playerNames = {"Pippo", "Pluto", "Topolino"};
//        try {
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
//            Board newBoard = new Board();
//            return new BoardPrinter(newBoard, playerList);
//        }catch(PositionOutOfBoundsException e){
//
//        }
//        return null;
//    }
}
