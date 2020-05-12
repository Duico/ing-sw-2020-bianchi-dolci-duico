package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventObservable;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.event.CardViewEvent;
import it.polimi.ingsw.view.event.ChallengerCardViewEvent;
import it.polimi.ingsw.view.event.FirstPlayerViewEvent;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli extends ClientViewEventObservable implements Runnable{
    final CliController cliController;
    boolean askNumPlayers = false;
    InputStream in;
    PrintStream out;
    public Cli(){
        in = System.in;
        out = System.out;
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
        while( (playerName = cliController.askName() ) ==null);
        if(askNumPlayers) {
            while ((numPlayers = cliController.askNumPlayers()) == null);
        }
        emitSignUp(new SignUpMessage(playerName, numPlayers));
    }

    protected void askFirstPlayer() {
        Player player;
        while( (player = cliController.askFirstPlayer()) == null);
        emitViewEvent(new FirstPlayerViewEvent(player));
    }
    protected void askCard(List<String> chosenCards){
        String cardName;
        while( (cardName = cliController.askCard(chosenCards)) == null);
        emitViewEvent(new CardViewEvent(cardName));
    }
    protected void askChallCards(List<String> cardDeck){
        List<String> challCards = cliController.askChallCards(cardDeck);
        emitViewEvent(new ChallengerCardViewEvent(challCards));
    }
    protected void printCorrectSignUp(boolean hasToWait){
        if(hasToWait){
            out.println(CliText.CORRECT_SIGNUP_WAIT.toString());
        }else{
            out.println(CliText.CORRECT_SIGNUP_LAST.toString());
        }
    }

}
