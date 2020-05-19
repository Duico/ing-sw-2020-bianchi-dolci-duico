package it.polimi.ingsw.client.cli;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.exception.PositionOutOfBoundsException;

import java.util.*;

public class BoardPrinter {

    private final List<Player> players;
    private final Board board;
    private final Map<Position, Player> workersMap;
    private int cellWidth = 2;
    private final int maxWidth = 320;

    public BoardPrinter(Board board, List<Player> players, Map<Position, Player> workersMap){
        this.board = board;
        this.players = players;
        this.workersMap = workersMap;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = Math.min( 10, Math.max(0, cellWidth));
    }

    private String getCharForNumber(int i) {
        return i >= 0 && i < 26 ? String.valueOf((char)(i + 65)) : null;
    }

    private Integer getNumberForChar(String s) {
        char c = s.charAt(0);
        return c >=65  && c < 91 ? c-65 : null;
    }

    public StringBuffer2D makeLettersHeader(){
        int actualWidth = getActualWidth();
        StringBuffer2D sb = new StringBuffer2D();
        //letters
        for(int y=0;y<board.getWidth();++y){
            sb.appendRow(0, " "+"_".repeat((int) Math.ceil(actualWidth/2)) +
                    Color.GREEN_UNDERLINED.escape(getCharForNumber(y)) +
                    "_".repeat((int) Math.ceil(actualWidth/2)));
        }
        return sb;
    }
    public StringBuffer2D makeNumberLegend(int number){
        StringBuffer2D sb = new StringBuffer2D();
        //empty first lines
        for(int i=0; i<cellWidth-1 && i<2; i++) {
            sb.appendln(Color.GREEN.escape("  "));
        }

        //worker line
        sb.appendln(Color.GREEN.escape(Integer.toString(number)+" "));

        //level and row first lines and row bottom
        for(int i=0; i<cellWidth-2 && i<1; i++) {
            sb.appendln(Color.GREEN.escape("  "));
        }
        for(int n=0; n<2; n++){
            sb.appendln(Color.GREEN.escape("  "));
        }
        return sb;
    }

    private String makeEmptyBoardLine(){
        return ("|" + Color.LIGHTGRAY.escape(" ".repeat(getActualWidth()) ) + "|");
    }
    private String makeDashBoardLine(boolean isTopLine){
        return (isTopLine?" ":"|"+ Color.LIGHTGRAY.escape( "_".repeat(getActualWidth()) ) + "|");
    }
    private String makeDashBoardLine(){
        return makeDashBoardLine(false);
    }
    private StringBuffer2D printDashBoardLine(boolean isTopLine){
        StringBuffer2D sb = new StringBuffer2D();
        for(int y=0;y<board.getWidth();++y){
            sb.appendRow(0, isTopLine?" ":"|"+"_".repeat(getActualWidth()));
        }
        sb.appendRow(0,"|");
        return sb;
    }

    private int getActualWidth(){
        return getActualWidth(false);
    }
    private int getActualWidth(boolean withColor){
        //5 is minimum width; cellWidth is side space; 7 is one color
        return 5+2*cellWidth + (withColor?2*7:0);
    }
    private int getActualHeight(){
        return Math.max(0, Math.min(cellWidth-1, 2)) + 2 + Math.max(0, Math.min(cellWidth-2, 1)) +1;
    }

    public StringBuffer2D printBoard(){
        StringBuffer2D sb = new StringBuffer2D();
        StringBuffer2D lettersHeader = makeLettersHeader();
                                    //hardcoded numberLegend width
        sb.insert(lettersHeader,2,0);
        //top row
        int actualWidth = getActualWidth(true)+1;
        //System.out.println(" ");
        //top border
        try{
        for(int y=0;y<board.getHeight();++y){
            int startY = lettersHeader.getHeight() + y*getActualHeight();
            //LEFT number headers
            StringBuffer2D numberLegend = makeNumberLegend(y+1);
            sb.insert(numberLegend, 0, startY);
            for(int x=0;x<board.getWidth();++x){
                Position position = new Position(x,y);
                StringBuffer2D dispCell = displayCell(position);
                int startX = numberLegend.getWidth() + x*actualWidth;
                sb.replace(dispCell, startX, startY, maxWidth, startY+getActualHeight());
            }
        }
        }catch(PositionOutOfBoundsException e){
            e.printStackTrace();
        }
        return sb;
    }

//    private StringBuffer2D printBoardRow(int y) throws PositionOutOfBoundsException{
//        //row first lines
////        for(int i=0; i<cellWidth-1 && i<2; i++) {
////            printEmptyBoardLine();
////        }
//        //worker
//        for(int x=0;x<board.getWidth();++x){
//            String lineOut = "| ";
//            Position position = new Position(x,y);
//            BoardCell cell = board.getBoardCell(position);
//            ViewPlayer workerPlayer =  getPlayerFromWorkersMap(position);
//
//            lineOut += " " + " ".repeat(cellWidth);
//            lineOut += displayWorker(workerPlayer);
//            lineOut += " " + " ".repeat(cellWidth);
//            //lineOut += displayCell(cell.getLevel(), cell.hasDome());
//            //lineOut += " ";
//            System.out.print(lineOut+" ");
//        }
//        System.out.println("|");
//        //level
//        for(int x=0;x<board.getWidth();++x){
//            String lineOut = "| ";
//            Position position = new Position(x,y);
//            BoardCell cell = board.getBoardCell(position);
//            ViewPlayer workerPlayer =  getPlayerFromWorkersMap(position);
//
//            //lineOut += displayWorker(workerPlayer);
//            lineOut += " "+" ".repeat(cellWidth);
//            lineOut += displayLevel(cell.getLevel(), cell.hasDome());
//            lineOut += " ".repeat(cellWidth);
//            System.out.print(lineOut+" ");
//        }
//        System.out.println("|");
//        //row first lines
//        for(int i=0; i<cellWidth-2 && i<1; i++) {
//            printEmptyBoardLine();
//        }
//        //row bottom border
//        printDashBoardLine(false);
//    }

    public StringBuffer2D displayCell(Position position){
        StringBuffer2D sb = new StringBuffer2D();
        //empty first lines
        for(int i=0; i<cellWidth-1 && i<2; i++) {
            sb.appendln(makeEmptyBoardLine());
        }

        //worker line
        BoardCell cell = board.getBoardCell(position);
        Player workerPlayer = getPlayerFromWorkersMap(position);
        String line1 = "| ";
        line1 += " ".repeat(cellWidth);
        line1 += " "+displayWorker(workerPlayer)+" ";
        line1 += " ".repeat(cellWidth);
        line1 +=" |";
        sb.appendln(line1);

        //level line
        String line2 = "|";
        line2 += " ".repeat(cellWidth);
        line2 += displayLevel(cell.getLevel(), cell.hasDome());
        line2 += " ".repeat(cellWidth);
        line2 += "|";
        sb.appendln(line2);

        //row first lines
        for(int i=0; i<cellWidth-2 && i<1; i++) {
            sb.appendln(makeEmptyBoardLine());
        }
        //row bottom border
        sb.appendln(makeDashBoardLine());
        return sb;
    }

    private String displayLevel(Level level, boolean hasDome){
        String out = "";
        Color c = Color.LIGHTGRAY;
        if(cellWidth<2){
            out += "  ";
            switch (level){
                case EMPTY:
                    out += "0";
                    break;
                case BASE:
                    out += "1";
                    break;
                case MID:
                    out += "2";
                    break;
                case TOP:
                    out += "3";
                    c = Color.RED;
                    break;
            }

            if(hasDome){
                out += "D";
                c = Color.BLUE;
            }else {
                out += " ";
            }
            out += " ";
        }else{
            if(hasDome){
                out += "DOME ";
                c = Color.BLUE;
            }else{
                switch(level){
                    case EMPTY:
                        out += "EMPTY";
                        break;
                    case BASE:
                        out += "BASE ";
                        break;
                    case MID:
                        out += " MID ";
                        break;
                    case TOP:
                        out += " TOP ";
                        c = Color.RED;
                        break;
                }
            }
        }
        return c.escape(out);
    }

    private String displayWorker(Player workerPlayer){
        String lineOut = "";
        if(workerPlayer!=null) {
            PlayerColor playerColor = workerPlayer.getColor();
            lineOut += Color.fromPlayerColor(playerColor, true).escape("W");
            //lineOut += "W";
        }else{
            lineOut += Color.LIGHTGRAY_BOLD.escape(" ");
            //lineOut += " ";
        }
        return lineOut;
//        return "W";
    }

    public StringBuffer2D printPlayers(){
        StringBuffer2D sb = new StringBuffer2D();
        int playerWidth = 15;
        String playerHeader = "Players";
        String godHeader = "God Card";
        sb.appendln(Color.LIGHTGRAY_UNDERLINED.escape(playerHeader)+" ".repeat(playerWidth-playerHeader.length())+Color.LIGHTGRAY_UNDERLINED.escape(godHeader));
        if(cellWidth >= 3) {
            sb.appendln("");
        }
        for(Player player : players){
            //todo
            String playerName = resizedPlayerName(player.getNickName(), playerWidth);
                                                                    //true if currentPlayer
            sb.appendln(Color.fromPlayerColor(player.getColor(), false).escape(playerName)+player.getCardName());
        }
        return sb;
    }
    private String resizedPlayerName(String name, int width){
        String result;
        Integer numSpaces = width-name.length();
        if(numSpaces<2){
            result = name.substring(0, width-4) + "... ";
        }else{
            result = name + " ".repeat(numSpaces);
        }
        return result;
    }

    public StringBuffer2D printHelp(boolean showDescription){
        final int commandWidth = 19;
        StringBuffer2D cmd = new StringBuffer2D();
        List<CommandTuple> commands = new ArrayList<>();
        commands.add(new CommandTuple("move A1 B1", "move worker in A1 to B1"));
        commands.add(new CommandTuple("build A1 B2 [dome]", "build in B2 with worker in A1"));
        commands.add(new CommandTuple("place A1", "place worker in A1"));
        commands.add(new CommandTuple("end", "end current turn"));
        commands.add(new CommandTuple("undo", "undo last operation"));
        commands.add(new CommandTuple("+/-", "make board bigger/smaller"));



        cmd.appendln(Color.LIGHTGRAY_UNDERLINED.escape("Commands usage"));
        if(cellWidth >= 3) {
            cmd.appendln("");
        }
        for(CommandTuple t : commands){
            cmd.appendln( t.getCommand() + " ".repeat(Math.max(0, commandWidth - t.getCommand().length())) + (showDescription?Color.CYAN.escape( t.getDescription() ):""));

        }
        return cmd;
    }
    class CommandTuple{
        private String command;
        private String description;
        public CommandTuple(String command, String description){
            this.command = command;
            this.description = description;
        }

        public String getCommand() {
            return command;
        }

        public String getDescription() {
            return description;
        }
    }
    public StringBuffer2D printAll(String infoMessage) {
        StringBuffer2D boardSB = printBoard();
        //HARDCODED 20
        int startX = boardSB.getWidth() + 2 + 20;
        int playersMaxWidth = 80;
        //print players
        boardSB.insert(printPlayers(), startX, 1, startX + playersMaxWidth);
        //print help on commands
        boardSB.insert(printHelp(cellWidth>=2), startX, (cellWidth >= 3)?6:5, maxWidth);
        //print infoMessage
        StringBuffer2D infoSB = new StringBuffer2D();
        infoSB.appendln(infoMessage);
        boardSB.insert(infoSB, startX, boardSB.getHeight()-2, maxWidth);
        return boardSB;
    }

    Player getPlayerFromWorkersMap(Position position){
        //if(workersMapOK == false){
//            generateWorkersMap();
//            workersMapOK = true;
        //}
        return workersMap.get(position);
    }

}
