package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;

import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Handler class for the cli input, line by line
 */
public class CliInputHandler extends ClientEventEmitter implements Runnable{
    private Queue<String> readLines = new LinkedBlockingQueue<>();
    public CliInputHandler(){
    }

    /**
     * Executes the input collection from stdin, enqueuing lines to readLines
     */
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

    /**
     * Polls readLines
     * @return the first non consumed line inserted into readLines
     */
    public String pollReadLines(){
        synchronized (this) {
            return readLines.poll();
        }
    }

    /**
     * Empty readLines. Useful after a new input request.
     */
    public void clearReadLines(){
        synchronized (this) {
            readLines.clear();
        }
    }
}
