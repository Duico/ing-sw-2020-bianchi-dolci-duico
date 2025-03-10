package it.polimi.ingsw.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class used to save and load Turn state in order to let players 'Undo' their last operation
 */
public class UndoBlob{
    private ByteArrayOutputStream ba;
    private Turn inputTurn;
    private Board inputBoard;
    private List<Player> inputPlayers;
    private Turn outputTurn;
    private Board outputBoard;
    private List<Player> outputPlayers;

    /**
     *
     * @param turn turn that you want to save
     * @param board board that you want to save
     * @param players list of player that you want to save
     */
    public UndoBlob(Turn turn, Board board, List<Player> players){
        inputTurn = turn;
        inputBoard = board;
        inputPlayers = players;
        outputPlayers = null;
        outputBoard = null;
        outputTurn = null;
        //only one undo per turn
        inputTurn.isUndoAvailable = false;
        writeBlob();
    }

    private ObjectOutputStream initOutputStream() throws IOException {
        ba = new ByteArrayOutputStream();
        return new ObjectOutputStream(ba);
    }

    private ObjectInputStream initInputStream() throws IOException {
        ByteArrayInputStream inputFile = new ByteArrayInputStream(ba.toByteArray());
        return new ObjectInputStream(inputFile);
    }

    /**
     * writes informations about Board, Turn and players on output stream
     */
    private void writeBlob(){
        ObjectOutputStream out = null;
        try {
            out = initOutputStream();
            out.writeObject(inputTurn);
            out.writeObject(inputBoard);
            out.writeObject(inputPlayers);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * reads informations about Board, Turn and players from input stream
     */
    private void readBlob(){
        ObjectInputStream in = null;
        Turn turn = null;
        Board board = null;
        ArrayList<Player> players = null;
        try {
            in = initInputStream();
            turn = (Turn) in.readObject();
            board = (Board) in.readObject();
            players = (ArrayList<Player>) in.readObject();
        } catch (IOException|ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(turn != null && players !=null) {
            outputTurn = turn;
            outputBoard = board;
            outputPlayers = players;
        }else{
            throw new NullPointerException();
        }
    }

    public Turn getTurn(){
        if(outputTurn==null){
            readBlob();
        }
        return outputTurn;
    }

    public Board getBoard(){
        if(outputTurn==null){
            readBlob();
        }
        return outputBoard;
    }

    public List<Player> getPlayers(){
        if(outputTurn==null){
            readBlob();
        }
        return outputPlayers;
    }
}
