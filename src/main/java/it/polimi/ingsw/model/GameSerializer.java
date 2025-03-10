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

    /**
     * saves game state on file
     * @param game current Game
     * @return True if game is successfully saved
     */
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

    /**
     * reads from file and loads the Game
     * @return Game object
     * @throws FileNotFoundException thrown if file is not found
     * @throws InvalidClassException thrown when serialization detects problems with a class
     */
    public Game readGame() throws FileNotFoundException, InvalidClassException{
        ObjectInputStream in = null;
        Game inputGame = null;
        try {
            in = initInputStream();
            inputGame = (Game) in.readObject();
            if(inputGame==null)
                return null;

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

        Position.height = 5;
        Position.width = 5;
        return inputGame;
    }

}
