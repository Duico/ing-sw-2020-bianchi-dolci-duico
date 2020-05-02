package it.polimi.ingsw.client;

public class ClientApp
{
    public static void main(String[] args){
        ClientView clientView = new ClientView();
        Client client = new Client("127.0.0.1", 12345);
        clientView.addObserverMove(client);
        clientView.addObserverLobby(client);
        clientView.addObserverBuild(client);
        clientView.addObserverPlace(client);
        clientView.addObserverEndTurn(client);
        clientView.addObserverUndo(client);

        Thread thread1 = new Thread(client);
        thread1.start();
        Thread thread2 = new Thread(clientView);
        thread2.start();

    }
}
