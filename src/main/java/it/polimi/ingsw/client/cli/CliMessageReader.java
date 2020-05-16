package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientConnection;
import it.polimi.ingsw.client.GameMessageVisitor;
import it.polimi.ingsw.server.message.GameMessage;

public class CliMessageReader implements Runnable{
    private ClientConnection clientConnection;
    private GameMessageVisitor gameMessageVisitor;

    public CliMessageReader(ClientConnection clientConnection, CliController cliController){
        this.clientConnection = clientConnection;
//        If needed, pass via constructor
//        this.gameMessageVisitor = gameMessageVisitor;
        this.gameMessageVisitor = new CliGameMessageVisitor(cliController);

    }
    @Override
    public void run(){
        while(clientConnection.isActive()) {
            GameMessage message;
//            while ( (message = clientConnection.pollReadMessages() ) == null){
//                try {
//                    clientConnection.wait();
//                } catch (InterruptedException e){
//                    e.printStackTrace();
//                }
//            }
            message = clientConnection.pollReadMessages();
            message.accept(gameMessageVisitor);
        }
    }
}
