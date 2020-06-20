package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;
import it.polimi.ingsw.view.event.*;

public class CommandParser {
    CliModel cliModel;
    public CommandParser(CliModel cliModel) {
        this.cliModel = cliModel;
    }
    public ViewEvent parseEvent(String cmd){
        cmd = cmd.toLowerCase();
        String[] words = cmd.split( "\\s{1,3}");
        String mainCommand = words[0];
        int numArgs = words.length;
        if(mainCommand.equals("move") && numArgs==3){
            Position workerPosition = parsePosition(words[1]);
            Position destPosition = parsePosition(words[2]);
            if(workerPosition == null || destPosition == null){
                return null;
            }
            return new MoveViewEvent(workerPosition, destPosition);
        }else if(mainCommand.equals("build") && (3==numArgs || 4==numArgs) ){
            Position workerPosition = parsePosition(words[1]);
            Position buildPosition = parsePosition(words[2]);
            if(workerPosition == null || buildPosition == null){
                return null;
            }
            boolean isDome;
            if(numArgs==4 && words[3].equals("dome")){
                isDome = true;
            }else if(numArgs==3){
                isDome = false;
            }else{
                return null;
            }
            return new BuildViewEvent(workerPosition, buildPosition, isDome);
        }else if(mainCommand.equals("place") && numArgs==2){
            Position destPosition = parsePosition(words[1]);
            if(destPosition == null){
                return null;
            }
            return new PlaceViewEvent(destPosition);
        }else if(mainCommand.equals("end") || mainCommand.equals("endturn") && numArgs==1){
            return new EndTurnViewEvent();
        } else if(mainCommand.equals("undo") && numArgs==1){
            return new UndoViewEvent();
        }
        return null;
    }
    private Position parsePosition(String positionString){
        if(positionString.length()!=2){
            return null;
        }
        char letter = positionString.charAt(0);
        int number = Integer.parseInt(positionString.substring(1));
        Integer boardWidth = cliModel.getBoardWidth();
        Integer boardHeight = cliModel.getBoardHeight();
        if(boardHeight == null || boardWidth == null) throw new RuntimeException("board not set in cliController");
//        int maxLetterUpper = 'A'+ Math.max(0, boardWidth-1);
        int maxLetterLower = 'a'+ Math.max(0, boardHeight-1);
//        if( (letter < 'A' || letter > 'Z' || letter > maxLetterUpper) && (letter < 'a' || letter > 'z' || letter > maxLetterLower) ){
        if(letter < 'a' || letter > 'z' || letter > maxLetterLower){
            return null;
        }
        if(number < 1 || number > boardHeight){
            return null;
        }
        int x = letter-'a';
        int y = number-1;
        try {
            Position position = new Position(x, y);
            return position;
        }catch (PositionOutOfBoundsException e){
            return null;
        }
    }

}
