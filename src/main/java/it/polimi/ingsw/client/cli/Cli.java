package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventObservable;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TurnPhase;
import it.polimi.ingsw.view.event.CardViewEvent;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.FirstPlayerViewEvent;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static it.polimi.ingsw.model.TurnPhase.*;

public class Cli extends ClientViewEventObservable implements Runnable{
    final CliController cliController;
    boolean askNumPlayers = false;
    Object askFirstPlayerLock = new Object();
    boolean hasPrintedTurnMessage = false;
    InputStream in;
    Scanner stdin;
    final PrintStream out;
    public Cli(){
        in = System.in;
        out = System.out;
        resetStdin();
        cliController = new CliController(in, out);
    }

    @Override
    public void run(){
        //  prompt for the user's name
        //askSetUpInfo(true);

        //cliController.printAll();
    }

    protected void askSetUpInfo(boolean askNumPlayers) {
        String playerName;
        Integer numPlayers = null;
        while( (playerName = promptName() ) ==null);
        if(askNumPlayers) {
            while ((numPlayers = promptNumPlayers()) == null);
        }
        emitSignUp(new SignUpMessage(playerName, numPlayers));
    }

    protected void askFirstPlayer() throws InterruptedException {
        //synchronized (out) {
        //    if (!hasPrintedTurnMessage)
        //        wait();
            Player player;
            while ((player = promptFirstPlayer()) == null) ;
            emitViewEvent(new FirstPlayerViewEvent(player));
        //    hasPrintedTurnMessage = false;
        //}
    }
    protected void askCard(List<String> chosenCards){
        String cardName;
        while( (cardName = promptCard(chosenCards)) == null);
        emitViewEvent(new CardViewEvent(cardName));
    }
    protected void askChallCards(List<String> cardDeck){
        List<String> challCards = promptChallCards(cardDeck);
        emitViewEvent(new ChallengerCardViewEvent(challCards));
    }
    protected void printCorrectSignUp(boolean hasToWait){
        if(hasToWait){
            out.println(CliText.CORRECT_SIGNUP_WAIT.toString());
        }else{
            out.println(CliText.CORRECT_SIGNUP_LAST.toString());
        }
    }
//
//    protected void printAll() {
//        synchronized (out) {
//            //while(!hasPrintedTurnMessage)
//            //    out.wait();
//            cliController.printAll();
//            //out.print(CliText.YOUR_TURN_COMMAND);
//            //hasPrintedTurnMessage = false;
//        }
//    }

    protected void nextTurn(boolean myTurn) {
//        boolean myTurn = cliController.getCurrentPlayer().equalsUuid(cliController.getCurrentPlayer());
        if(myTurn) {
            out.println();
            out.print(CliText.YOUR_TURN.toString());
        }else{
            out.println();
            out.print(CliText.WAIT_TURN.toString(cliController.getCurrentPlayer().getNickName()));
        }
        switch (cliController.getTurnPhase()) {
            case CHOSE_CARDS:
                //cliController.printAll();
                break;
            case PLACE_WORKERS:
            case NORMAL:
                cliController.printAll();
                break;
        }
    }

    private String promptName(){
        out.print(CliText.ASK_NAME.toPrompt());
        resetStdin();
        String line = stdin.nextLine().trim();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            out.println(CliText.BAD_NAME);
            return null;
        }
        return line;
    }
    private Player promptFirstPlayer(){
        StringBuilder playersLine = new StringBuilder();
        boolean isFirst = true;
        for(Player player: cliController.getPlayers()){
            if(player.equalsUuid(cliController.getMyPlayer())){
                continue;
            }
            playersLine.append( (isFirst ? "" : ", ") + player.getNickName() );
            isFirst = false;
        }
        out.print(CliText.ASK_FIRSTPLAYER.toPrompt(playersLine.toString()));
        resetStdin();
        final String line = stdin.nextLine().trim();
        if(!line.matches("^[A-Za-z0-9\\-_]{3,32}\\s*$")){
            out.println(CliText.BAD_NAME);
            return null;
        }
        //check if line is in players
        List<Player> matchingPlayers;

        if((matchingPlayers = cliController.getPlayers().stream().filter( (p)-> (p.getNickName().equals(line)) ).collect(Collectors.toList()) ).size() == 0){
            out.println(CliText.BAD_PLAYERNAME.toString(line));
            return null;
        }
        return matchingPlayers.get(0);
    }

    private Integer promptNumPlayers(){
        out.print(CliText.ASK_NUMPLAYERS.toPrompt());
        Integer line = stdin.nextInt();
        if(line<2 || line >3){
            out.println(CliText.BAD_NUMPLAYERS);
            return null;
        }
        return line;
    }

    private List<String> promptChallCards(List<String> cards){
        List<String> chosenCards = new ArrayList<>();
        String line;
        int numPlayers = cliController.getNumPlayers();
        if(numPlayers < 2) throw new RuntimeException("Players not set in the cliController");
        while( chosenCards.size() < numPlayers){
            resetStdin();
            CliText cliText = (chosenCards.size()==0)?CliText.ASK_CHALLCARD_FIRST:CliText.ASK_CHALLCARD_MORE;
            out.print(cliText.toPrompt(cards.toString()));
            line = stdin.nextLine().trim();
            if (cards.contains(line)) {
                chosenCards.add(line);
                cards.remove(line);
                out.print(CliText.OK_CHALLCARD().toString(line));
            } else {
                out.print(CliText.BAD_CARD.toString(cards.toString()));
            }

        }
        return chosenCards;
    }
    private String promptCard(List<String> chosenCards) {
        String line;
        out.print(CliText.ASK_CARD.toPrompt(chosenCards.toString()));
        resetStdin();
        line = stdin.nextLine().trim();
        if (!chosenCards.contains(line)) {
            out.print(CliText.BAD_CARD.toString(chosenCards.toString()));
            line = null;
        }
        return line;
    }


    private void resetStdin() {
        stdin = new Scanner(in);
    }

}
