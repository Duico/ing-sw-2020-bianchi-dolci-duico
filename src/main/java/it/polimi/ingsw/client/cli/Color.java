package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.model.PlayerColor;

/**
 * enum which contains some colors for the cli
 */
public enum Color {
    //Normal
    RED("\u001B[0;31m"),
    GREEN("\u001B[0;32m"),
    YELLOW("\u001B[0;33m"),
    BLUE("\u001B[0;34m"),
    PURPLE("\u001B[0;35m"),
    CYAN("\u001B[0;36m"),
    GRAY("\u001B[0;90m"),
    WHITE("\\u001B[0;39m"),
    LIGHTGRAY("\u001B[0;37m"),
    //Bold
    BLACK_BOLD ( "\033[1;30m"),
    RED_BOLD ( "\033[1;31m"),
    GREEN_BOLD ( "\033[1;32m"),
    YELLOW_BOLD ( "\033[1;33m"),
    BLUE_BOLD ( "\033[1;34m"),
    PURPLE_BOLD ( "\033[1;35m"),
    CYAN_BOLD ( "\033[1;36m"),
    GRAY_BOLD("\u001B[1;90m"),
    WHITE_BOLD( "\033[1;39m"),
    LIGHTGRAY_BOLD( "\033[1;37m"),
    //Underlined
    BLACK_UNDERLINED( "\033[4;30m"),
    RED_UNDERLINED ( "\033[4;31m"),
    GREEN_UNDERLINED ( "\033[4;32m"),
    YELLOW_UNDERLINED ( "\033[4;33m"),
    BLUE_UNDERLINED ( "\033[4;34m"),
    PURPLE_UNDERLINED ( "\033[4;35m"),
    CYAN_UNDERLINED ( "\033[4;36m"),
    GRAY_UNDERLINED("\u001B[4;90m"),
    WHITE_UNDERLINED( "\033[4;39m"),
    LIGHTGRAY_UNDERLINED( "\033[4;37m");


    static final String RESET = "\u001B[0;00m";

    private String escape;

    Color(String escape) {
        this.escape = escape;
    }
    public String escape(String message) {
        return escape+message+RESET;
    }

    /**
     * function which for a player color return the color for the display
     * @param playerColor
     * @param bold
     * @return
     */
    public static Color fromPlayerColor(PlayerColor playerColor, boolean bold){
        switch(playerColor){
            case BLUE:
                return bold?BLUE_BOLD:BLUE;
            case GRAY:
                return bold? LIGHTGRAY_BOLD:LIGHTGRAY;
            case YELLOW:
                return bold?YELLOW_BOLD:YELLOW;
            default:
                return bold? WHITE_BOLD:WHITE;
        }
    }
}
