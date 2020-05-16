package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventEmitter;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class CliInputHandler extends ClientViewEventEmitter implements Runnable{
    private Queue<String> readLines = new LinkedBlockingQueue<>();
    public CliInputHandler(){
    }
    @Override
    public void run(){
        Scanner stdin = new Scanner(System.in);
        while (stdin.hasNextLine()){
            readLines.add(stdin.nextLine());
            System.out.println("got it!");
        }
    }
    public String pollReadLines(){
        return readLines.poll();
    }
    public void clearReadLines(){
        readLines.clear();
    }
}
