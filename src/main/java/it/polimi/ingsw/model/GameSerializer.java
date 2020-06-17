package it.polimi.ingsw.model;

import java.io.*;

/**
 * Manages writing and reading game state to file, to enable persistency
 */
public class GameSerializer {
    private String filename;

    /**
     *
     * @param filename Base name of the file to write to
     */
    public GameSerializer(String filename){
        this.filename = filename;
    }

//    /**
//     * Serialize game Game to filename
//     */
//    public void saveGame(Game game){
//        GameSerializer serializer = new GameSerializer(filename);
//        serializer.writeGame(game);
//    }
//
//    /**
//     * Return deserialized Game
//     */
//    public Game loadGame(){
//        GameSerializer serializer =  new GameSerializer(filename);
//        return (Game) serializer.readGame();
//    }

    private ObjectOutputStream initOutputStream() throws IOException {
        FileOutputStream outputFile = new FileOutputStream(this.filename);
        ObjectOutputStream outputStream = new ObjectOutputStream(outputFile);
        return outputStream;
    }

    private ObjectInputStream initInputStream() throws IOException {
        FileInputStream inputFile = new FileInputStream(this.filename);
        ObjectInputStream inputStream = new ObjectInputStream(inputFile);
        return inputStream;
    }

    public boolean writeGame(Game game){
        boolean success = false;
        ObjectOutputStream out = null;
        try {
            out = initOutputStream();
            out.writeObject(game);
            success = true;
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
        return success;
    }

    public Game readGame() throws FileNotFoundException, InvalidClassException{
        ObjectInputStream in = null;
        Game inputGame = null;
        try {
            in = initInputStream();
            inputGame = (Game) in.readObject();
            //undoBlob is transient
            //we need to regenerate it on reload from persistency
            if(inputGame==null)
                return null;

//          game.undoBlob is not serializable
            inputGame.regenerateUndo();
        }catch (InvalidClassException | FileNotFoundException e){
            throw e;
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
        //FIX set static attributes
        Position.height = 5;
        Position.width = 5;
        return inputGame;
    }

}
