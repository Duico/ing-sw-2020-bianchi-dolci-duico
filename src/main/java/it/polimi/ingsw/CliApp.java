package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.event.ClientConnectionEventListener;
import it.polimi.ingsw.client.message.SignUpListener;
import it.polimi.ingsw.view.ViewEventListener;


public class CliApp {
    public static void main( String[] args )
    {
//        Queue<Object> toSendMessages = new LinkedBlockingQueue<>();
        Cli cli = new Cli();
        CliModel cliModel = new CliModel();

        //create visitors for all event types
//        CliModelEventVisitor modelEventVisitor = new CliModelEventVisitor(cli, cliModel);
//        CliControllerResponseVisitor controllerResponseVisitor = new CliControllerResponseVisitor(cli, cliModel);
//        CliSetUpMessageVisitor setUpMessageVisitor = new CliSetUpMessageVisitor(cli, cliModel);

        GameMessageVisitor gameMessageVisitor = new CliGameMessageVisitor(cli, cliModel);
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345, gameMessageVisitor);
//        clientConnection.addEventListener(ClientConnectionEventListener.class, cli);
        gameMessageVisitor.addSignUpListener(clientConnection);
        gameMessageVisitor.addViewEventListener(clientConnection);

        //CliMessageReader cliMessageReader = new CliMessageReader(cli, clientConnection, cliController);


        //Thread cliMessageReaderThread = new Thread(cliMessageReader);
        Thread cliInputHandlerThread = new Thread(cli);
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

}
