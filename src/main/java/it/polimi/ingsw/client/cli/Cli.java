package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;

import java.io.*;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Cli implements /*ClientConnectionEventListener,*/ Runnable {
    //boolean hasPrintedTurnMessage = false;
//    InputStream in;
//    Scanner stdin;
    final PrintStream out;
    private Integer BPcellWidth = 2;
    private boolean isAwaitingInput;
    //final CliInputHandler inputHandler = new CliInputHandler();
    final CliInputHandler inputHandler;
    private final String ANSI_CLS = "\u001b[2J";

    //    private Queue<CliRunnable> cliRunnableQueue = new LinkedBlockingQueue<>();
    private final ExecutorService inputRequestsPool = Executors.newCachedThreadPool();

    // StringWriter infoString = new StringWriter();
    // PrintWriter infoOut = new PrintWriter(infoString);
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
        //System.out.flush();
    }

    public Integer getBPcellWidth() {
        return BPcellWidth;
    }

    public void increaseBPcellWidth() {
        BPcellWidth = Math.min(10, Math.max(0, ++BPcellWidth));
    }

    public void decreaseBPcellWidth() {
        BPcellWidth = Math.min(10, Math.max(0, --BPcellWidth));
    }

    public boolean isAwaitingInput() {
        return isAwaitingInput;
    }

    public void setAwaitingInput(boolean awaitingInput) {
        isAwaitingInput = awaitingInput;
    }

    //    protected void execInputRequest(CliLambda lambda){
//        //passing this to the runnable
//        executorPool.submit( () -> {
//            lambda.execute(this);
//        });
//
//    }
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

//    protected void handleInput(LineConsumer lambda){
//            new Thread( () -> {
//                synchronized (inputHandler) {
//                    inputHandler.clearReadLines();
//                    String line;
//                    while ((line = inputHandler.pollReadLines()) == null) {
//                        try {
//                            inputHandler.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    lambda.consumeLine(line);
//                }
//            });
//    }

    protected String pollLine() throws InterruptedException {
        synchronized (inputHandler) {
            String line;
            while ((line = inputHandler.pollReadLines()) == null) {
                inputHandler.wait();
            }
            return line;
        }
    }

    public void clearReadLines() {
        inputHandler.clearReadLines();
    }


    public void println(Object o) {
        //?? in a separate thread??
        out.println(o);
    }

    public void print(Object o) {
        //?? in a separate thread??
        out.print(o);
    }


    public void print(StringBuffer2D sb) {
        sb.printOut(out);
    }

    public void printAll(BoardPrinter bp, boolean myTurn, String infoMessage) {
        clear();
        bp.setCellWidth(getBPcellWidth());
        print(" " + System.lineSeparator() + System.lineSeparator());
        print(bp.printAll(infoMessage));
        CliText promptText = myTurn ? CliText.YOUR_TURN_COMMAND : CliText.ENTER_COMMAND;
        print(promptText.toPrompt());
    }

    public void printClientConnectionEvent(ConnectionMessage evt) {

        System.err.print(evt.getType().toString());
        //inputHandler.shutdown();
    }

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

        //inputHandler.shutdown();
    }
}