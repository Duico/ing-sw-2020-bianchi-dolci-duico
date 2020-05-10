package it.polimi.ingsw.client.cli;

import java.io.BufferedWriter;
import java.io.StringWriter;

public class BufferedWriterTest {
    public static void main(String[] args){
        StringBuffer s = new StringBuffer(120);
        //StringWriter sw = new StringWriter(120);

        s.append("down", 2, 4);
        s.insert(1, "ABCDEFGHILMNOPQRSTU", 1, 10);
        s.replace(1, 2, "ZZZZZZZZZ".substring(0,2));
        s.append(".Pippo");
        System.out.println(s);
    }
}
