package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientViewEventObservable;

import java.io.PrintStream;
import java.util.Scanner;

public class Cli extends ClientViewEventObservable implements Runnable{
    final CliController cliController;
    public Cli(){
        Scanner scannerIn = new Scanner(System.in);
        PrintStream out = System.out;
        cliController = new CliController(scannerIn, out);
    }

    @Override
    public void run(){
        //  prompt for the user's name
        cliController.askName();
    }
}
