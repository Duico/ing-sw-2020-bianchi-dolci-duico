package it.polimi.ingsw;

import com.sun.javafx.application.ParametersImpl;
import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.MessageVisitor;
import it.polimi.ingsw.client.cli.*;
import it.polimi.ingsw.client.event.MessageListener;
import javafx.application.Application;

import java.util.Map;


public class CliApp {
    public static void main( String[] args )
    {
        Application.Parameters params = new ParametersImpl(args);
        TcpParamsParser parser = new TcpParamsParser(params);
        String paramsIp = "127.0.0.1";
        Integer paramsPort = 38612;
        try {
            paramsIp = parser.getIp();
        }catch (TcpParamsParser.IllegalParameterException e){
            System.out.println("Missing or wrong IP address inserted. Reverting to default.");
        }
        try{
            paramsPort = parser.getPort();
        } catch (TcpParamsParser.IllegalParameterException e) {
            System.out.println("Missing or wrong port selected. Reverting to default.");
        }
        System.out.println("Connecting to "+paramsIp+":"+paramsPort);
        Cli cli = new Cli();
        CliModel cliModel = new CliModel();

        //create visitors for all event types
//        CliModelEventVisitor modelEventVisitor = new CliModelEventVisitor(cli, cliModel);
//        CliControllerResponseVisitor controllerResponseVisitor = new CliControllerResponseVisitor(cli, cliModel);
//        CliSetUpMessageVisitor setUpMessageVisitor = new CliSetUpMessageVisitor(cli, cliModel);

        MessageVisitor gameMessageVisitor = new CliMessageVisitor(cli, cliModel);
        ClientConnection clientConnection = new ClientConnection(paramsIp, paramsPort, gameMessageVisitor);
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
