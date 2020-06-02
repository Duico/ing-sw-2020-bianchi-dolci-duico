package it.polimi.ingsw;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.event.MessageListener;


public class CliApp {
    public static void main( String[] args )
    {

        Cli cli = new Cli();
        CliModel cliModel = new CliModel();

        //create visitors for all event types
//        CliModelEventVisitor modelEventVisitor = new CliModelEventVisitor(cli, cliModel);
//        CliControllerResponseVisitor controllerResponseVisitor = new CliControllerResponseVisitor(cli, cliModel);
//        CliSetUpMessageVisitor setUpMessageVisitor = new CliSetUpMessageVisitor(cli, cliModel);

        MessageVisitor gameMessageVisitor = new CliMessageVisitor(cli, cliModel);
        ClientConnection clientConnection = new ClientConnection("3.137.63.131", 12192, gameMessageVisitor);
        clientConnection.addEventListener(MessageListener.class, gameMessageVisitor);
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


    }

}
