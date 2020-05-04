package it.polimi.ingsw.client.cli;

import java.util.ArrayList;
import java.util.List;

public class StringBuffer2D {
    private List<StringBuffer> rows;
    private int initWidth;
    StringBuffer2D(int width){
        initWidth = width;
        rows = new ArrayList<>(40);
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
        return true;
    }
    public boolean appendRow(int y, String s){
        StringBuffer row = getRowSafe(y);
        row.append(s);
        return true;
    }
    public boolean replaceRow(int y, String s, int startX, int endX){
        StringBuffer row = getRowSafe(y);
        extendRow(row, startX);
        row.replace(startX, endX, s.substring(0, endX-startX));
        return true;
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
        StringBuffer row = rows.get(i);
        if(row == null){
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
}
