package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventEmitter;

import java.util.Scanner;

public class CliInputHandler extends ClientViewEventEmitter implements Runnable{
    public CliInputHandler(){

    }
    @Override
    public void run(){
        Scanner stdin = new Scanner(System.in);
        while (stdin.hasNextLine()){
            stdin.nextLine();
            System.out.println("got it!");
        }
    }
}
