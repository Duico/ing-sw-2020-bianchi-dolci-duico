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
        while (true) {
                Scanner stdin = new Scanner(System.in);
                //while (stdin.hasNextLine()) {
                readLines.add(stdin.nextLine());
            synchronized (this) {
                    this.notifyAll();
            }
                    System.err.println("readLines.size() = "+readLines.size());
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
