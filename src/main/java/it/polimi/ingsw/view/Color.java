package it.polimi.ingsw.view;

public enum Color {
    //Normal
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),
    //Bold
    BLACK_BOLD ( "\033[1;30m"),
    RED_BOLD ( "\033[1;31m"),
    GREEN_BOLD ( "\033[1;32m"),
    YELLOW_BOLD ( "\033[1;33m"),
    BLUE_BOLD ( "\033[1;34m"),
    PURPLE_BOLD ( "\033[1;35m"),
    CYAN_BOLD ( "\033[1;36m"),
    WHITE_BOLD ( "\033[1;37m"),
    //Underlined
    BLACK_UNDERLINED( "\033[4;30m"),
    RED_UNDERLINED ( "\033[4;31m"),
    GREEN_UNDERLINED ( "\033[4;32m"),
    YELLOW_UNDERLINED ( "\033[4;33m"),
    BLUE_UNDERLINED ( "\033[4;34m"),
    PURPLE_UNDERLINED ( "\033[4;35m"),
    CYAN_UNDERLINED ( "\033[4;36m"),
    WHITE_UNDERLINED ( "\033[4;37m");


    static final String RESET = "\u001B[0m";

    private String escape;

    Color(String escape) {
        this.escape = escape;
    }
    public String escape() {
        return escape;
    }
}
