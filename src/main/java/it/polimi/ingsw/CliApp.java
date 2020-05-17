package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.view.ViewEventListener;


public class CliApp {
    public static void main( String[] args )
    {
//        Queue<Object> toSendMessages = new LinkedBlockingQueue<>();
        CliModel cliModel = new CliModel();
        CliInputHandler cliInputHandler = new CliInputHandler();
        Cli cli = new Cli(cliInputHandler);
        GameMessageVisitor gameMessageVisitor = new CliGameMessageVisitor(cli, cliModel);
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345, gameMessageVisitor);


        //CliMessageReader cliMessageReader = new CliMessageReader(cli, clientConnection, cliController);
        cliInputHandler.addEventListener(ViewEventListener.class, clientConnection);
        cliInputHandler.addEventListener(SignUpListener.class, clientConnection);

        //Thread cliMessageReaderThread = new Thread(cliMessageReader);
        Thread cliInputHandlerThread = new Thread(cliInputHandler);
        //clientConnection.run();
        //cliMessageReaderThread.start();
        cliInputHandlerThread.start();
        clientConnection.run();
//        connectionThread.start();
        try{
            //cliMessageReaderThread.join();
            cliInputHandlerThread.join();
//            connectionThread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

//        Thread thread2 = new Thread(cliVisitor);
//        //thread2.start();
//        try{
//            thread2.join();
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }


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
