package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventObservable;
import it.polimi.ingsw.client.message.SignUpMessage;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cli extends ClientViewEventObservable implements Runnable{
    final CliController cliController;
    boolean askNumPlayers = false;
    Scanner in;
    PrintStream out;
    public Cli(){
        in = new Scanner(System.in);
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
    protected void printCorrectSignUp(boolean hasToWait){
        if(hasToWait){
            out.println(CliText.CORRECT_SIGNUP_WAIT.toString());
        }else{
            out.println(CliText.CORRECT_SIGNUP_LAST.toString());
        }
    }

}
