package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientEventEmitter;
import it.polimi.ingsw.client.ModelEventVisitor;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.model.event.*;
import it.polimi.ingsw.view.event.CardViewEvent;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.FirstPlayerViewEvent;
import it.polimi.ingsw.view.event.ViewEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CliModelEventVisitor extends ClientEventEmitter implements ModelEventVisitor {
    private Cli cli;
    private final CliModel cliModel;
    public CliModelEventVisitor(Cli cli, CliModel cliModel){
        this.cli = cli;
        this.cliModel = cliModel;
    }

    @Override
    public void visit(BuildWorkerModelEvent evt) {
        Position startPosition = evt.getStartPosition();
        Position destinationPosition = evt.getDestinationPosition();
        boolean isDome = evt.isDome();
        cliModel.buildWorker(startPosition, destinationPosition, isDome);
        nextOperation();
    }

    @Override
    public void visit(MoveWorkerModelEvent evt) {
        Position startPosition = evt.getStartPosition();
        Position destinationPosition = evt.getDestinationPosition();
        Position pushPosition = evt.getPushPosition();
        Player player = evt.getPlayer();
        //cliModel.moveWorker(destinationPosition, pushPosition);
        cliModel.moveWorker(startPosition, destinationPosition, pushPosition, player);
        cliModel.moveBoard(startPosition, destinationPosition, pushPosition);
//        cliModel.updatePlayer(player);
        nextOperation();
    }

    @Override
    public void visit(PlaceWorkerModelEvent evt) {
        Player player = evt.getPlayer();
        cliModel.updatePlayer(player);
        cliModel.placeWorker(evt.getPlacePosition());
        nextOperation();
    }

    @Override
    public void visit(ChosenCardsModelEvent evt) {
                //System.out.println(Color.YELLOW_BOLD.escape("Chosen Cards"));
        List<String> cardDeck = evt.getCardDeck();
        List<String> cards = evt.getChosenCards();
        Player player = evt.getPlayer();

        //TODO remove
//        System.err.print("ChosenCards "+evt.toString());

        cliModel.setCards(cards);
        cliModel.setCardDeck(cardDeck);
        if(cliModel.getTurnPhase().equals(TurnPhase.CHOSE_CARDS)){
            if(player.equalsUuid(cliModel.getMyPlayer())) { //my Turn to chose
                printChoseCards();
            }
        }
    }

    @Override
    public void visit(FailModelEvent evt) {

    }

    @Override
    public void visit(FullInfoModelEvent evt) {
        cliModel.setBoard(evt.getBoard());
        cliModel.setPlayers(evt.getPlayers());
    }

    @Override
    public void visit(NewTurnModelEvent evt) {
        Player player = evt.getPlayer();
            TurnPhase turnPhase = evt.getTurnPhase();
            cliModel.setTurnPhase(turnPhase);
            //use before setPlayersIfNotSet
            cliModel.updatePlayer(player);
            cliModel.setPlayersIfNotSet(evt.getPlayers());
            cliModel.setCurrentPlayer(player);
            boolean myTurn = player.equalsUuid(cliModel.getMyPlayer());
//            if (player.equalsUuid(cliController.getMyPlayer())) {
//                myTurn = true;
//                switch (turnPhase) {
//                    case CHOSE_CARDS:
//                        break;
//                    case PLACE_WORKERS:
//                    case NORMAL:
//                        //do nothing (cliController.setTurnPhase is enough)
//                        //wait for command to be input
//                        break;
//                }
//            } else {
//                myTurn = false;
//            }
            //hasPrintedTurnMessage = true;
            //out.notifyAll();

        //printed inside printAll
//            if (myTurn) {
//                cli.print(System.lineSeparator() + CliText.YOUR_TURN.toString());
//            } else {
//                cli.print(System.lineSeparator() + CliText.WAIT_TURN.toString(cliModel.getCurrentPlayer().getNickName()));
//            }
            nextOperation(myTurn);
    }

    @Override
    public void visit(PlayerDefeatModelEvent evt) {
        Player playerDefeat = evt.getPlayer();
        if(cliModel.getMyPlayer().getUuid().equals(playerDefeat.getUuid())) {
            printAll(CliText.LOSER_BLOCK.toString());
        } else{
            printAll(CliText.ADVISE_LOSER_BLOCK.toString(playerDefeat.getNickName()));
        }
        cliModel.removePlayer(playerDefeat);


    }

    @Override
    public void visit(WinModelEvent evt) {
        Player winner = evt.getPlayer();
        if(cliModel.getMyPlayer().getUuid().equals(winner.getUuid())) {
            printAll(CliText.WINNER.toString());
        } else{
            printAll(CliText.LOSER.toString(winner.getNickName()));
        }
    }

    @Override
    public void visit(SetCardModelEvent evt) {
        /*System.out.println(evt.getPlayer().getNickName());
        System.out.println(evt.getPlayer().getCard().getName());*/
        cliModel.setPlayerCard(evt.getPlayer(), evt.getCard());
        boolean isOwnCard = cliModel.getMyPlayer().equalsUuid(evt.getPlayer());
        CliText cliText;
        if(isOwnCard){
            cliText = CliText.SET_CARD_OWN;
        }else{
            cliText = CliText.SET_CARD_OTHER;
        }
        //cliModel.updatePlayer(evt.getPlayer());
        cli.print(System.lineSeparator() + cliText.toString(evt.getCard().getName(), evt.getPlayer().getNickName()));


        /*cli.execInputRequest( () -> {
        CliText cliText;
            if(isOwnCard){
                cliText = CliText.SET_CARD_OWN;
            }else{
                cliText = CliText.SET_CARD_OTHER;
            }

            //TODO generalize
            //ORIGINALLY infoOut instead of cli
                //cli.print(System.lineSeparator() + cliText.toString(evt.getCardName(), evt.getPlayer().getNickName()));
        });*/
//        askFirstPlayerLock.notifyAll();

    }

    @Override
    public void visit(UndoModelEvent evt) {
       for (Player player : evt.getPlayers())
           cliModel.updatePlayer(player);

       cliModel.setBoard(evt.getBoard());
       nextOperation();

    }
    protected void nextOperation(){
        nextOperation(cliModel.isMyTurn());
    }
    protected void nextOperation(boolean myTurn) {
//        out.print(infoString.getBuffer());
//        //TODO
//        infoString = new StringWriter();
//        infoOut = new PrintWriter(infoString);

        String turnText = myTurn?CliText.YOUR_TURN.toString():CliText.WAIT_TURN.toString(cliModel.getCurrentPlayer().getNickName());
        switch (cliModel.getTurnPhase()) {
            case CHOSE_CARDS:
                cli.print(System.lineSeparator().repeat(2) + turnText);
                break;
            case PLACE_WORKERS:
            case NORMAL:
                //distinguish between your turn and other player's
                printAll(myTurn, turnText);
                awaitCommands();
//                println("NEW TURN");
                break;
        }
    }

        private void printChoseCards(){
        List<String> cards = cliModel.getCards();
        List<String> cardDeck = cliModel.getCardDeck();

        //isMyTurn already checked in caller function

        if (cards == null) { //pick cards
            if (cardDeck != null) {
                cli.execInputRequest(askChallCards(cardDeck));
            } else {
                throw new RuntimeException("Both cards and cardDeck are null");
            }
        } else if (cards.size() == 1) {
            //server will automatically pick last card for you
            //ASK firstPlayer
            cli.execInputRequest(askFirstPlayer());


        } else if (cards.size() > 1) {
            cli.execInputRequest(askCard(cards));
        }
    }

    protected CliRunnable askFirstPlayer() {
        return () -> {
            Player player;
//            try {
                while ((player = promptFirstPlayer()) == null) ;
                emitViewEvent(new FirstPlayerViewEvent(player));
//            }catch (InterruptedException e){
//                return;
//            }
        };
    }
    protected CliRunnable askCard(List<String> chosenCards){
        return () -> {
            String cardName;
//            try {
                while ((cardName = promptCard(chosenCards)) == null) ;
//            }catch (InterruptedException e){
//                return;
//            }
            emitViewEvent(new CardViewEvent(cardName));
        };
    }
    protected CliRunnable askChallCards(List<String> cardDeck){
        return () -> {
            List<String> challCards = null;
//            try {
                challCards = promptChallCards(cardDeck);
//            } catch (InterruptedException e) {
//                return;
//            }
            emitViewEvent(new ChallengerCardViewEvent(challCards));
        };
    }

    private Player promptFirstPlayer() throws InterruptedException{
        StringBuilder playersLine = new StringBuilder();
        boolean isFirst = true;
        for(Player player: cliModel.getPlayers()){
            if(player.equalsUuid(cliModel.getMyPlayer())){
                continue;
            }
            playersLine.append( (isFirst ? "" : ", ") + player.getNickName() );
            isFirst = false;
        }
        cli.print(CliText.ASK_FIRSTPLAYER.toPrompt(playersLine.toString()));
        cli.clearReadLines();
        final String line = cli.pollLine().trim();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            cli.println(CliText.BAD_NAME);
            return null;
        }
        //check if line is in players
        List<Player> matchingPlayers;

        if((matchingPlayers = cliModel.getPlayers().stream().filter( (p)-> (p.getNickName().equals(line)) ).collect(Collectors.toList()) ).size() == 0){
            cli.println(CliText.BAD_PLAYERNAME.toString(line));
            return null;
        }
        return matchingPlayers.get(0);
    }

    private List<String> promptChallCards(List<String> cards) throws InterruptedException {
        List<String> chosenCards = new ArrayList<>();
        String line;
        int numPlayers = cliModel.getNumPlayers();
        if(numPlayers < 2) throw new RuntimeException("Players not set in the cliModel");
        while( chosenCards.size() < numPlayers){
            CliText cliText = (chosenCards.size()==0)?CliText.ASK_CHALLCARD_FIRST:CliText.ASK_CHALLCARD_MORE;
            cli.print(cliText.toPrompt(cards.toString()));
            cli.clearReadLines();
            line = cli.pollLine().trim();
            if (cards.contains(line)) {
                chosenCards.add(line);
                cards.remove(line);
                cli.print(CliText.OK_CHALLCARD().toString(line));
            } else {
                cli.print(CliText.BAD_CARD.toString(cards.toString()));
            }

        }
        return chosenCards;
    }
    private String promptCard(List<String> chosenCards) throws InterruptedException {
        String line;
        cli.print(CliText.ASK_CARD.toPrompt(chosenCards.toString()));
        cli.clearReadLines();
        line = cli.pollLine().trim();
        if (!chosenCards.contains(line)) {
            cli.print(CliText.BAD_CARD.toString(chosenCards.toString()));
            line = null;
        }
        return line;
    }

    private void printAll(){
        printAll(cliModel.isMyTurn(), cliModel.getLastInfoMessage());
    }
    private void printAll(boolean myTurn){
        printAll(myTurn, cliModel.getLastInfoMessage());
    }
    private void printAll(String infoMessage){
        printAll(cliModel.isMyTurn(), infoMessage);
    }
    private void printAll(boolean myTurn, String infoMessage) {
//        return () -> {
            cliModel.setLastInfoMessage(infoMessage);
            BoardPrinter bp = cliModel.createBoardPrinter();
            cli.printAll(bp, myTurn, infoMessage);
            //command is read by another thread
//        };
    }
//    protected CliRunnable askCommand(){
//        return () -> {
//            String cmd;
//            while ((cmd = promptCommand(cliModel.isMyTurn())) == null || !executeCommand(cliModel.isMyTurn(), cmd));
//        };
//        //});
//    }
    private void awaitCommands() {
        if(!cli.isAwaitingInput()) {
            CliRunnable inputGetter = () -> {
                while (true) {
                    String cmd;
//                    try {
                        while (true) {
                                if (!((cmd = readCommand()) == null || !executeCommand(cliModel.isMyTurn(), cmd)));
                        }
//                    } catch (InterruptedException e) {
//                        return;
//                    }
                }
                //cli.setAwaitingInput(false) on exit
            };
            cli.setAwaitingInput(true);
            cli.execAsyncInputRequest(inputGetter);
        }
    }
    private String readCommand() throws InterruptedException {
        String line;

        //needed to avoid Exception below

        line = cli.pollLine().trim();


        if (line.equals("")) {
            return null;
        } else if(!line.matches("^([A-Za-z\\-\\+][A-Za-z0-9\\-\\+]{0,60}\\s{0,3}){1,6}$")){
            printAll(CliText.BAD_COMMAND.toString());
//            cli.println(CliText.BAD_COMMAND);
            return null;
        }
        return line;
    }
//    private String promptCommand(boolean myTurn){
//        CliText promptText = myTurn?CliText.YOUR_TURN_COMMAND:CliText.ENTER_COMMAND;
//        cli.print(promptText.toPrompt());
//        return readCommand();
//    }
    private boolean executeCommand(boolean myTurn, String cmd){
        CommandParser commandParser;
        ViewEvent event = null;
        if(cmd.equals("+")){
            cli.increaseBPcellWidth();
            printAll();
        }else if(cmd.equals("-")){
            cli.decreaseBPcellWidth();
            printAll();
        }else{
            commandParser = new CommandParser(cliModel);
            event = commandParser.parseEvent(cmd);
            if(event == null){
                printAll(CliText.BAD_COMMAND.toString());
//                cli.execInputRequest(printAll);
                return false;
            }else{
                if (myTurn) {
                    emitViewEvent(event);
                }else{
                    printAll(CliText.WAIT_TURN_RED.toString(cliModel.getCurrentPlayer().getNickName()));
                }
            }
        }
        //printAll on WorkerModelEvent
        //TODO remove
//        if(event == null){
//            printAll(myTurn);
//        }
        return true;
    }
}
