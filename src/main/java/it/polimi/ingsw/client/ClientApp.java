/*package it.polimi.ingsw.client;

public class ClientApp
{
    public static void main(String[] args){
        ClientGui clientGui = new ClientGui();
        ClientConnection clientConnection = new ClientConnection("127.0.0.1", 12345);
        clientGui.addViewEventListener(clientConnection);

        Thread thread1 = new Thread(clientConnection);
        thread1.start();
        Thread thread2 = new Thread(clientGui);
        thread2.start();

    }
}
*/