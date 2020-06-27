package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;

import java.util.NoSuchElementException;
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

                try {
                    readLines.add(stdin.nextLine());
                }catch(NoSuchElementException e){
                    //do nothing
                }
            synchronized (this) {
                this.notify();
            }

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

    protected void shutdown() {

        System.exit(0);
    }
}
