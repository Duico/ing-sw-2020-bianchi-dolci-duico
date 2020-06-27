package it.polimi.ingsw.client.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Two-dimensional String buffer.
 * Useful for pasting contents in the cli on top of each other without having to print line by line.
 */
public class StringBuffer2D {
    private List<StringBuffer> rows;
    private int width = 0;
    private final int maxWidth = 640;

    public StringBuffer2D(){
        rows = new ArrayList<>(40);
    }

    /**
     *
     * @param initString String to be set as the first line
     */
    public StringBuffer2D(String initString){
        this();
        appendRow(0, initString);
    }

    /**
     * Replaces the characters in the region [startX, endX]X[startY, endY] with those from toInsert
     * @param toInsert StringBuffer2D to be used as a replacement
     * @param startX X coord of first vertex
     * @param startY Y coord of first vertex
     * @param endX X coord of the second vertex
     * @param endY Y coord o the second vertex
     * @return this
     */
    public StringBuffer2D replace(StringBuffer2D toInsert, int startX, int startY, int endX, int endY){
        populateRows(endY);
        for(int i=startY; i<endY; i++){
            replaceRow(i, toInsert.getRowSafe(i-startY).toString(), startX, endX);
        }
        return this;
    }

    /**
     * Insert all rows from toInsert to the current one
     * @param toInsert StringBuffer2D to be inserted
     * @param startX coord of the ending column
     * @param startY coord of the starting row
     * @param endX coord of the ending column
     * @return
     */
    public StringBuffer2D insert(StringBuffer2D toInsert, int startX, int startY, int endX){
        int endY = startY+toInsert.getHeight();
        populateRows(endY);
        for(int i=startY; i<endY; i++){
            insertRow(i, toInsert.getRowSafe(i-startY).toString(), startX, endX);
        }
        return this;
    }
    /**
     * Insert all rows from toInsert to the current one. Coord of the ending column is automatically set to the maximun possible
     * @param toInsert StringBuffer2D to be inserted
     * @param startX coord of the ending column
     * @param startY coord of the starting row
     * @return
     */
    public StringBuffer2D insert(StringBuffer2D toInsert, int startX, int startY){
        return insert(toInsert, startX, startY, maxWidth);
    }

    /**
     * Insert s to row y of this object, starting from column startX and ending not after column endX
     * @param y Row to be edited
     * @param s String to be inserted
     * @param startX Start column for insertion
     * @param endX End column for insertion
     * @return false if String.insert() has errors
     */
    public boolean insertRow(int y, String s, int startX, int endX){
        StringBuffer row = getRowSafe(y);
        extendRow(row, startX);
        int initWidth = row.length();
        int insertEnd = Math.min(s.length(), endX - startX);
        row.insert(startX, s, 0, insertEnd);
        width = Math.max(width,  initWidth + insertEnd);
        //false if insert has errors
        return true;
    }

    /**
     * Append s to row w of this.
     * @param y Row to be edited
     * @param s String to be inserted
     * @return true
     */
    public boolean appendRow(int y, String s){
        StringBuffer row = getRowSafe(y);
        int initWidth = row.length();
        row.append(s);
        width = Math.max(width, initWidth + s.length());
        return true;
    }

    /**
     * Replace characters from startX to endX in row y of this with s
     * @param y Row to be edited
     * @param s String to be inserted
     * @param startX coord of the starting column
     * @param endX coord of the ending column
     * @return false if errored
     */
    public boolean replaceRow(int y, String s, int startX, int endX){
        int endIndex = Math.min(endX-startX, s.length());
        return replaceRow(y, s, startX, endX, endIndex);
    }

    /**
     * Replace characters from startX to endX in row y of this with s, truncated at endIndex
     * @param y Row to be edited
     * @param s String to be inserted
     * @param startX coord of the starting column
     * @param endX coord of the ending column
     * @param endIndex length at which s will be truncated
     * @return false if errored
     */
    public boolean replaceRow(int y, String s, int startX, int endX, int endIndex){
        StringBuffer row = getRowSafe(y);
        extendRow(row, startX);
        int initWidth = row.length();
        row.replace(startX, endX, s.substring(0, endIndex));
        width = Math.max(width,  initWidth + (startX - endX) + s.length());
        return true;
    }

    /**
     * Append a line with content s
     * @param s String to become content of the new line
     * @return appendRow return value
     */
    public boolean appendln(String s){
        return appendRow(rows.size(), s);
    }

    public int getHeight(){
        return rows.size();
    }
    public int getWidth(){
        return width;
    }

    private void extendRow(StringBuffer row, int minWidth){
        int repCount = minWidth-row.length();
        if(repCount > 0){
            row.append(" ".repeat(minWidth - row.length()));
        }
    }


    private StringBuffer getRowSafe(int i){
        StringBuffer row;
        while(rows.size() <= i || (row = rows.get(i)) == null){
            row = new StringBuffer();

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


    public void printOut(PrintStream out){
        for(StringBuffer row: rows){
            out.println(row);
        }
    }
}
