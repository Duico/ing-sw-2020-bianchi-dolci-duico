package it.polimi.ingsw.client.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class StringBuffer2D {
    private List<StringBuffer> rows;
    StringBuffer2D(){
        rows = new ArrayList<>(40);
    }
    StringBuffer2D(String initString){
        this();
        appendRow(0, initString);
    }
    public StringBuffer2D replace(StringBuffer2D toInsert, int startX, int startY, int endX, int endY){
        populateRows(endY);
        for(int i=startY; i<endY; i++){
            replaceRow(i, toInsert.getRowSafe(i-startY).toString(), startX, endX);
        }
        return this;
    }
    public StringBuffer2D insert(StringBuffer2D toInsert, int startX, int startY, int endX){
        int endY = startY+toInsert.getHeight();
        populateRows(endY);
        for(int i=startY; i<endY; i++){
            insertRow(i, toInsert.getRowSafe(i-startY).toString(), startX, endX);
        }
        return this;
    }
    public boolean insertRow(int y, String s, int startX, int endX){
        StringBuffer row = getRowSafe(y);
        extendRow(row, startX);
        row.insert(startX, s, 0, endX-startX);
        //false if insert has errors
        return true;
    }
    public boolean appendRow(int y, String s){
        StringBuffer row = getRowSafe(y);
        row.append(s);
        return true;
    }
    public boolean replaceRow(int y, String s, int startX, int endX){
        int endIndex = Math.min(endX-startX, s.length());
        return replaceRow(y, s, startX, endX, endIndex);
    }
    public boolean replaceRow(int y, String s, int startX, int endX, int endIndex){
        StringBuffer row = getRowSafe(y);
        extendRow(row, startX);
        row.replace(startX, endX, s.substring(0, endIndex));
        return true;
    }
    public boolean appendln(String s){
        return appendRow(rows.size(), s);
    }

    public int getHeight(){
        return rows.size();
    }

    private void extendRow(StringBuffer row, int minWidth){
        int repCount = minWidth-row.length();
        if(repCount > 0){
            row.append(" ".repeat(minWidth - row.length()));
        }
    }

    //todo move downwards hierarchy
    private StringBuffer getRowSafe(int i){
        StringBuffer row;
        while(rows.size() <= i || (row = rows.get(i)) == null){
            row = new StringBuffer();
            //row = new StringBuffer(" ".repeat(initWidth));
            rows.add(row);
        }
        return row;
    }

    private boolean populateRows(int endY){
        for(int i=0; i<endY; i++){
            getRowSafe(i);
        }
        return true;
    }

    public void printOut(){
        for(StringBuffer row : rows){
            System.out.println(row);
        }
    }
    public void printOut(PrintStream out){
        for(StringBuffer row: rows){
            out.println(row);
        }
    }
}
