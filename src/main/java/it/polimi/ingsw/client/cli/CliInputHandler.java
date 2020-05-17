package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;

import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class CliInputHandler extends ClientEventEmitter implements Runnable{
    private Queue<String> readLines = new LinkedBlockingQueue<>();
    public CliInputHandler(){
    }
    @Override
    public void run(){
        while (true) {
                Scanner stdin = new Scanner(System.in);
                //while (stdin.hasNextLine()) {
                readLines.add(stdin.nextLine());
            synchronized (this) {
                    this.notify();
            }
//                    System.err.println("readLines.size() = "+readLines.size());
                //}
        }
    }
    public String pollReadLines(){
        synchronized (this) {
            return readLines.poll();
        }
    }
    public void clearReadLines(){
        synchronized (this) {
            readLines.clear();
        }
    }
}
