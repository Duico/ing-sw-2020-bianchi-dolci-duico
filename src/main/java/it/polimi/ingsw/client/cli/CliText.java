package it.polimi.ingsw.client.cli;

import java.util.List;
import java.util.Random;

public enum CliText {
    ASK_NAME("Insert your nickname:"),
    ASK_NUMPLAYERS("Insert number of players:"),
    BAD_NAME(Color.RED.escape("Nickname format is not valid")),
    BAD_PLAYERNAME(Color.RED.escape("There is no player with nickname \"%s\"")),
    BAD_NUMPLAYERS(Color.RED.escape("Insert number of players:")),
    CORRECT_SIGNUP_WAIT(Color.GREEN.escape("Correct signup... wait for the other players")),
    CORRECT_SIGNUP_LAST(Color.GREEN.escape("Correct signup. Starting new game.")),
    YOUR_TURN(Color.YELLOW.escape("Your turn to play.")),
    WAIT_TURN(Color.YELLOW.escape("%s's turn to play. Wait...")),
    YOUR_TURN_COMMAND("Your turn to play. Enter command:"),
    ASK_CHALLCARD_FIRST("Chose one card from the deck %s:"),
    ASK_CHALLCARD_MORE("Chose another card from the deck %s:"),
    OK_CHALLCARD1(Color.YELLOW.escape("%s, nice pick.")),
    OK_CHALLCARD2(Color.YELLOW.escape("%s, good choice.")),
    OK_CHALLCARD3(Color.YELLOW.escape("Oh, %s... well chosen.")),
    //BAD_CHALLCARD(Color.RED.escape("Invalid card name, chose from %s:")),
    ASK_CARD("Chose one of the cards selected by the Challenger %s:"),
    BAD_CARD(Color.RED.escape("Invalid card name, chose one of %s:")),
    SET_CARD(Color.YELLOW.escape("Card %s set for player %s.")),
    ASK_FIRSTPLAYER("Chose the Start Player [%s]:"),
    INVALID_NICKNAME(Color.RED.escape("Invalid nickName")),
    INVALID_NUMPLAYERS(Color.RED.escape("Incorrect num of players")),
    ENTER_COMMAND("Enter command:"),
    ;

    static CliText OK_CHALLCARD(){
        CliText[] phrases = {OK_CHALLCARD1, OK_CHALLCARD2, OK_CHALLCARD3};
        return phrases[(new Random()).nextInt(phrases.length)];
    }


    CliText(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public String toString(Object ... args) {
        return String.format(toString(), args);
    }
    public String toPrompt(){
        return "\r\n"+ text + "\r\n> ";
    }
    public String toPrompt(Object ... args){
        return String.format(toPrompt(), args);
    }

    private final String text;
}
