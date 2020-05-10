package it.polimi.ingsw.client.cli;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StringBuffer2DTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private PrintStream outPrintStream;

    @BeforeEach
    void createOut(){
        outPrintStream = new PrintStream(outContent);
    }
    @Test
    void initialization(){
        StringBuffer2D sb= new StringBuffer2D();
        sb.appendln("Ciao a tutti");
        sb.printOut(outPrintStream);
        assertEquals("Ciao a tutti\n", outContent.toString());
    }
    @Test
    void initializationString(){
        StringBuffer2D sb= new StringBuffer2D("Ciao a tutti");
        sb.printOut(outPrintStream);
        assertEquals("Ciao a tutti\n", outContent.toString());
    }
    @Test
    void appendRowUnordered(){
        StringBuffer2D sb= new StringBuffer2D("Buona giornata");
        sb.appendRow(2, "Saluti!");
        sb.appendRow(0, " ");
        sb.appendRow(0, "a tutti");
        sb.printOut(outPrintStream);
        assertEquals("Buona giornata a tutti\n\nSaluti!\n", outContent.toString());
    }
    @Test
    void insertRow(){
        StringBuffer2D sb= new StringBuffer2D("Buona giornata");
        sb.appendln("Saluti!");
        String ins = " a tutti";
        sb.insertRow(1, ins, 6, 6+ins.length());
        sb.printOut(outPrintStream);
        assertEquals("Buona giornata\nSaluti a tutti!\n", outContent.toString());
    }
    @Test
    void replaceRow(){
        StringBuffer2D sb = new StringBuffer2D("Oggi e' una brutta giornata");
        sb.replaceRow(0, "bella", 12, 18);
        sb.printOut(outPrintStream);
        assertEquals("Oggi e' una bella giornata\n", outContent.toString());
    }
    @Test
    void replaceRowWithIndex(){
        StringBuffer2D sb = new StringBuffer2D("Oggi e' una brutta giornata");
        sb.replaceRow(0, "magnifica", 12, 18, "magnifica".length());
        sb.printOut(outPrintStream);
        assertEquals("Oggi e' una magnifica giornata\n", outContent.toString());

    }
    //TODO test 2D functions... making the assertions is too difficult
}