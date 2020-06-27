package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class which prints messages and request input commands
 */
public class Cli implements Runnable {

    final PrintStream out;
    private Integer BPcellWidth = 2;
    private boolean isAwaitingInput;
    final CliInputHandler inputHandler;
    private final String ANSI_CLS = "\u001b[2J";
    private final ExecutorService inputRequestsPool = Executors.newCachedThreadPool();

    PrintStream infoOut;

    public Cli() {
        out = System.out;
        infoOut = out;
        this.inputHandler = new CliInputHandler();
        isAwaitingInput = false;
    }

    @Override
    public void run() {
        inputHandler.run();
    }

    public void clear() {
        System.out.print(ANSI_CLS);

    }

    public Integer getBPcellWidth() {
        return BPcellWidth;
    }

    /**
     * Function which increases the display width of a cell
     */
    public void increaseBPcellWidth() {
        BPcellWidth = Math.min(10, Math.max(0, ++BPcellWidth));
    }

    /**
     * Function which decreases the display width of a cell
     */
    public void decreaseBPcellWidth() {
        BPcellWidth = Math.min(10, Math.max(0, --BPcellWidth));
    }

    public boolean isAwaitingInput() {
        return isAwaitingInput;
    }

    public void setAwaitingInput(boolean awaitingInput) {
        isAwaitingInput = awaitingInput;
    }


    protected void execAsyncInputRequest(CliRunnable cliRunnable) {
        //passing this to the runnable
        inputRequestsPool.submit(() -> {
            try {
                cliRunnable.run();
            } catch (InterruptedException e) {
                return;
            }
        });
    }

    protected void execInputRequest(CliRunnable cliRunnable) {
        //passing this to the runnable
        inputRequestsPool.submit(() -> {
            synchronized (this) {
                try {
                    cliRunnable.run();
                } catch (InterruptedException e) {
                    return;
                }
            }
        });
    }

    protected void shutdown() {
        inputRequestsPool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
//            if (!inputRequestsPool.awaitTermination(1, TimeUnit.SECONDS)) {
            inputRequestsPool.shutdownNow(); // Cancel currently executing tasks
            // Wait a while for tasks to respond to being cancelled
            if (!inputRequestsPool.awaitTermination(10, TimeUnit.SECONDS))
                System.err.println("Some threads did not terminate");
//            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            inputRequestsPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


    protected String pollLine() throws InterruptedException {
        synchronized (inputHandler) {
            String line;
            while ((line = inputHandler.pollReadLines()) == null) {
                inputHandler.wait();
            }
            return line;
        }
    }

    /**
     * Function who clear all the messages to print
     */
    public void clearReadLines() {
        inputHandler.clearReadLines();
    }


    public void println(Object o) {
        out.println(o);
    }

    public void print(Object o) {
        out.print(o);
    }


    public void print(StringBuffer2D sb) {
        sb.printOut(out);
    }

    /**
     * Function which display all the information about a normal turn
     * It prints the board, the name of the players, a lists of commands
     * and info messages
     * @param bp
     * @param myTurn
     * @param infoMessage
     * @param endGame
     */
    public void printAll(BoardPrinter bp, boolean myTurn, String infoMessage, boolean endGame) {
        clear();
        bp.setCellWidth(getBPcellWidth());
        print(" " + System.lineSeparator() + System.lineSeparator());
        print(bp.printAll(infoMessage));
        CliText promptText = myTurn ? CliText.YOUR_TURN_COMMAND : CliText.ENTER_COMMAND;
        if (!endGame)
            print(promptText.toPrompt());
    }

    public void printClientConnectionEvent(ConnectionMessage evt) {

        System.err.print(evt.getType().toString());
    }


    /**
     * Function which based on the ClientConnectionEvent.Reason, prints a message
     * @param evt
     */
    public void printClientConnectionEvent(ClientConnectionEvent evt) {

        if (evt.getReason().equals(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET)) {
            System.err.print("Error on the connection with the server! Please, retry..");
        } else if (evt.getReason().equals(ClientConnectionEvent.Reason.CONNECTION_LOST)) {
            System.err.print("The connection with the server is lost....");
        } else if (evt.getReason().equals(ClientConnectionEvent.Reason.PING_FAIL)) {
            System.err.print("The connection with the server is lost....");
        } else if (evt.getReason().equals(ClientConnectionEvent.Reason.IO_EXCEPTION)) {
            System.err.print("IO exception....");
        } else if (evt.getReason().equals(ClientConnectionEvent.Reason.INTERRUPTED)) {
            System.err.print("interrupted....");
        } else if (evt.getReason().equals(ClientConnectionEvent.Reason.SOCKET_EXCEPTION)) {
            System.err.print("socket exception....");
        }

    }
}