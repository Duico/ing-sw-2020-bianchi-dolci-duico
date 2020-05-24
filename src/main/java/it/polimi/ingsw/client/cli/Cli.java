package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public Cli(){
        //this.inputHandler = inputHandler; //todo TEMP
        out = System.out;
        infoOut = out;
//        cliModel = new CliModel();
        this.inputHandler= new CliInputHandler();
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
        inputRequestsPool.submit(cliRunnable::run);
    }

    protected void execInputRequest(CliRunnable cliRunnable) {
        //passing this to the runnable
        inputRequestsPool.submit(() -> {
            synchronized (this) {
                cliRunnable.run();
            }
        });
    }

    //TODO
    protected void shutdown() {
//        inputRequestsPool.shutdown();
        inputRequestsPool.shutdownNow();
//        System.exit(0);
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

    protected String pollLine() {
        synchronized (inputHandler) {
            String line;
            while ((line = inputHandler.pollReadLines()) == null) {
                try {
                    inputHandler.wait();
                } catch (InterruptedException e) {
                    System.err.println("Richiesta di input interrotta.");
                    //e.printStackTrace();
                }
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
            print(" "+System.lineSeparator()+System.lineSeparator());
            print(bp.printAll(infoMessage));
            //TODO command prompt HERE
            CliText promptText = myTurn ? CliText.YOUR_TURN_COMMAND : CliText.ENTER_COMMAND;
            print(promptText.toPrompt());
    }

    public void printClientConnectionEvent(ConnectionMessage evt){

        System.err.print(evt.getType().toString());
        //inputHandler.shutdown();
    }

    public void printClientConnectionEvent(ClientConnectionEvent evt){

        if(evt.getReason().equals(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET)){
            System.err.print("Error on the connection with the server! Please, retry..");
        } else if(evt.getReason().equals(ClientConnectionEvent.Reason.CONNECTION_LOST)){
            System.err.print("The connection with the server is lost....");
        } else if(evt.getReason().equals(ClientConnectionEvent.Reason.PING_FAIL)){
            System.err.print("The connection with the server is lost....");
        } else if(evt.getReason().equals(ClientConnectionEvent.Reason.IO_EXCEPTION)){
            System.err.print("IO exception....");
        } else if(evt.getReason().equals(ClientConnectionEvent.Reason.INTERRUPTED)){
            System.err.print("interrupted....");
        } else if(evt.getReason().equals(ClientConnectionEvent.Reason.SOCKET_EXCEPTION)){
            System.err.print("socket exception....");
        }

        //inputHandler.shutdown();
    }

    /*
    @Override
    public void handleEvent(ClientConnectionEvent evt) {
        System.err.print(evt.getReason().toString());
        clear();
//        System.exit(0);
    }

    @Override
    public void handleEvent(GameMessage evt) {

    }*/




//    protected String pollLine (Runnable lambda){
//        String line;
//        new Thread( () -> {
//            synchronized (inputHandler) {
//                inputHandler.clearReadLines();
//                while (inputHandler.pollReadLines() == null) {
//                    inputHandler.wait();
//                }
//            return line;
//            }
//        }).join();
//        inputHandler.pollReadLines();
//    }

//    protected void askSetUpInfo(boolean askNumPlayers){
//        lockOut( () -> {
//            String playerName;
//            Integer numPlayers = null;
//            while ((playerName = promptName()) == null) ;
//            if (askNumPlayers) {
//                while ((numPlayers = promptNumPlayers()) == null) ;
//            }
//            emitSignUp(new SignUpMessage(playerName, numPlayers));
//        });
//    }
//
//    protected void askFirstPlayer() {
//        lockOut( () -> {
//            Player player;
//            while ((player = promptFirstPlayer()) == null) ;
//            emitViewEvent(new FirstPlayerViewEvent(player));
//        });
//    }
//    protected void askCard(List<String> chosenCards){
//        lockOut( () -> {
//            String cardName;
//            while ((cardName = promptCard(chosenCards)) == null) ;
//            emitViewEvent(new CardViewEvent(cardName));
//        });
//    }
//    protected void askChallCards(List<String> cardDeck){
//        lockOut( () -> {
//            List<String> challCards = promptChallCards(cardDeck);
//            emitViewEvent(new ChallengerCardViewEvent(challCards));
//        });
//    }
//
//    protected void printCorrectSignUp(boolean hasToWait){
//        lockOut( () -> {
//            if (hasToWait) {
//                out.println(CliText.CORRECT_SIGNUP_WAIT.toString());
//            } else {
//                out.println(CliText.CORRECT_SIGNUP_LAST.toString());
//            }
//        });
//    }
////
////    protected void printAll() {
////        synchronized (out) {
////            //while(!hasPrintedTurnMessage)
////            //    out.wait();
////            cliController.printAll();
////            //out.print(CliText.YOUR_TURN_COMMAND);
////            //hasPrintedTurnMessage = false;
////        }
////    }
//    protected void nextOperation(){
//        nextOperation(false, cliController.isMyTurn());
//    }
//    protected void nextOperation(boolean isNewTurn, boolean myTurn) {
////        boolean myTurn = cliController.getCurrentPlayer().equalsUuid(cliController.getCurrentPlayer());
//        synchronized (cliController) {
//            while (cliController.getTurnPhase().equals(TurnPhase.CHOSE_CARDS) && cliController.isTurnOK == false) {
//               try{
//                cliController.wait();
//               }catch (InterruptedException e){
//                   e.printStackTrace();
//               }
//            }
//        }
//
//        if(isNewTurn) {
//            if (myTurn) {
//                out.println();
//                out.print(CliText.YOUR_TURN.toString());
//            } else {
//                out.println();
//                out.print(CliText.WAIT_TURN.toString(cliController.getCurrentPlayer().getNickName()));
//            }
//        }
////        out.print(infoString.getBuffer());
////        //TODO
////        infoString = new StringWriter();
////        infoOut = new PrintWriter(infoString);
//
//        switch (cliController.getTurnPhase()) {
//            case CHOSE_CARDS:
//                if(myTurn) {
//                    printChoseCards();
//                }
//                break;
//            case PLACE_WORKERS:
//            case NORMAL:
//                //distinguish between your turn and other player's
//                printAll(myTurn);
//                break;
//        }
//    }
//
//    private void printChoseCards(){
//        List<String> cards = cliController.getCards();
//        List<String> cardDeck = cliController.getCardDeck();
//        //NOTE: no lockOut here because it is already in the single functions
//
////        if(!cliController.getMyPlayer().equalsUuid(cliController.choseCardPlayer)){
////            return;
////        }
//            if (cards == null) { //pick cards
//                if (cardDeck != null) {
//                    askChallCards(cardDeck);
//                } else {
//                    //error
//                }
//            } else if (cards.size() == 1) {
//                //server will automatically pick last card for you
//                //ASK firstPlayer
////            synchronized(askFirstPlayerLock) {
////                    while (cliController.getPlayerCard(cliController.getMyPlayer()) == null) {
////                        askFirstPlayerLock.wait();
////                    }
//                askFirstPlayer();
//
//
//            } else if (cards.size() > 1) {
//                askCard(cards);
//            }
//    }
//    private void printAll(){
//        printAll(cliController.isMyTurn());
//    }
//    private void printAll(boolean myTurn) {
//        lockOut( () -> {
//            bp = cliController.createBoardPrinter();
//            bp.setCellWidth(BPcellWidth);
//            out.print(" " + System.lineSeparator() + System.lineSeparator());
//            bp.printAll().printOut(out);
//        });
//        askCommand(myTurn);
//    }
//    protected void askCommand(boolean myTurn){
//        //lockOut( () -> {
//            String cmd;
//            while ((cmd = promptCommand(myTurn)) == null || !executeCommand(myTurn, cmd));
//        //});
//    }
//    private String promptCommand(boolean myTurn){
//        CliText promptText = myTurn?CliText.YOUR_TURN_COMMAND:CliText.ENTER_COMMAND;
//        out.print(promptText.toPrompt());
//        String line = null;
//
//        //needed to avoid Exception below
//        pollLine();
//
//        if(!line.matches("^([A-Za-z\\-\\+][A-Za-z0-9\\-\\+]{0,60}\\s{0,3}){1,6}$")){
//            out.println(CliText.BAD_COMMAND);
//            return null;
//        }
//        return line;
//    }
////    Exception in thread "Thread-44" java.lang.IndexOutOfBoundsException: end
////    at java.base/java.util.regex.Matcher.region(Matcher.java:1515)
////    at java.base/java.util.Scanner.findPatternInBuffer(Scanner.java:1089)
////    at java.base/java.util.Scanner.findWithinHorizon(Scanner.java:1791)
////    at java.base/java.util.Scanner.nextLine(Scanner.java:1649)
////    at it.polimi.ingsw.client.cli.Cli.promptCommand(Cli.java:193)
////    at it.polimi.ingsw.client.cli.Cli.askCommand(Cli.java:185)
////    at it.polimi.ingsw.client.cli.Cli.printAll(Cli.java:180)
////    at it.polimi.ingsw.client.cli.Cli.nextOperation(Cli.java:137)
////    at it.polimi.ingsw.client.cli.CliModelEventVisitor.visit(CliModelEventVisitor.java:181)
////    at it.polimi.ingsw.controller.response.FailedOperationControllerResponse.accept(FailedOperationControllerResponse.java:32)
////    at it.polimi.ingsw.client.ClientConnection$1.lambda$run$0(ClientConnection.java:71)
////    at java.base/java.lang.Thread.run(Thread.java:830)
//
//    private boolean executeCommand(boolean myTurn, String cmd){
//        CommandParser commandParser;
//        ViewEvent event = null;
//        if(cmd.equals("+")){
//            BPcellWidth++;
//        }else if(cmd.equals("-")){
//            BPcellWidth--;
//        }else{
//            commandParser = new CommandParser(cliController);
//            event = commandParser.parse(cmd);
//            if(event == null){
//                out.println(CliText.BAD_COMMAND);
//                return false;
//            }else{
//              emitViewEvent(event);
//            }
//        }
//        //printAll on WorkerModelEvent
//        //TODO remove
//        if(event == null){
//            printAll(myTurn);
//        }
//        return true;
//    }
//    private String promptName(){
//        out.print(CliText.ASK_NAME.toPrompt());
//        resetStdin();
//        //if(!stdin.hasNextLine())
//        String line = stdin.nextLine().trim();
//        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
//            out.println(CliText.BAD_NAME);
//            return null;
//        }
//        return line;
//    }
//    private Player promptFirstPlayer(){
//        StringBuilder playersLine = new StringBuilder();
//        boolean isFirst = true;
//        for(Player player: cliController.getPlayers()){
//            if(player.equalsUuid(cliController.getMyPlayer())){
//                continue;
//            }
//            playersLine.append( (isFirst ? "" : ", ") + player.getNickName() );
//            isFirst = false;
//        }
//        out.print(CliText.ASK_FIRSTPLAYER.toPrompt(playersLine.toString()));
//        resetStdin();
//        final String line = stdin.nextLine().trim();
//        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
//            out.println(CliText.BAD_NAME);
//            return null;
//        }
//        //check if line is in players
//        List<Player> matchingPlayers;
//
//        if((matchingPlayers = cliController.getPlayers().stream().filter( (p)-> (p.getNickName().equals(line)) ).collect(Collectors.toList()) ).size() == 0){
//            out.println(CliText.BAD_PLAYERNAME.toString(line));
//            return null;
//        }
//        return matchingPlayers.get(0);
//    }
//
//    private Integer promptNumPlayers(){
//        out.print(CliText.ASK_NUMPLAYERS.toPrompt());
//        resetStdin();
//        while(!stdin.hasNextInt()) {
//
//        }
////            out.println(CliText.BAD_NUMPLAYERS_STRING);
////            return null;
//
//        Integer line = stdin.nextInt();
//        //burn a line
//        if(stdin.hasNextLine())
//            stdin.nextLine();
//        if(line<2 || line >3){
//            out.println(CliText.BAD_NUMPLAYERS_INT);
//            return null;
//        }
//        return line;
//    }
//
//    private List<String> promptChallCards(List<String> cards){
//        List<String> chosenCards = new ArrayList<>();
//        String line;
//        int numPlayers = cliController.getNumPlayers();
//        if(numPlayers < 2) throw new RuntimeException("Players not set in the cliController");
//        while( chosenCards.size() < numPlayers){
//            CliText cliText = (chosenCards.size()==0)?CliText.ASK_CHALLCARD_FIRST:CliText.ASK_CHALLCARD_MORE;
//            out.print(cliText.toPrompt(cards.toString()));
//            resetStdin();
//            line = stdin.nextLine().trim();
//            if (cards.contains(line)) {
//                chosenCards.add(line);
//                cards.remove(line);
//                out.print(CliText.OK_CHALLCARD().toString(line));
//            } else {
//                out.print(CliText.BAD_CARD.toString(cards.toString()));
//            }
//
//        }
//        return chosenCards;
//    }
//    private String promptCard(List<String> chosenCards) {
//        String line;
//        out.print(CliText.ASK_CARD.toPrompt(chosenCards.toString()));
//        resetStdin();
//        line = stdin.nextLine().trim();
//        if (!chosenCards.contains(line)) {
//            out.print(CliText.BAD_CARD.toString(chosenCards.toString()));
//            line = null;
//        }
//        return line;
//    }

}
