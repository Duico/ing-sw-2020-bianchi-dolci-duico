package it.polimi.ingsw.client;

public class ClientApp
{
    public static void main(String[] args){
        ClientGui clientGui = new ClientGui();
        ClientGuiConnection clientGuiConnection = new ClientGuiConnection("127.0.0.1", 12345);
        clientGui.addObserverMove(clientGuiConnection);
        clientGui.addObserverLobby(clientGuiConnection);
        clientGui.addObserverBuild(clientGuiConnection);
        clientGui.addObserverPlace(clientGuiConnection);
        clientGui.addObserverEndTurn(clientGuiConnection);
        clientGui.addObserverUndo(clientGuiConnection);
        clientGui.addObserverChalCards(clientGuiConnection);
        clientGui.addObserverSetCard(clientGuiConnection);
        clientGui.addObserverFirstPlayer(clientGuiConnection);

        Thread thread1 = new Thread(clientGuiConnection);
        thread1.start();
        Thread thread2 = new Thread(clientGui);
        thread2.start();

    }
}
