package it.polimi.ingsw.client.cli;

import java.util.List;
import java.util.Random;

public enum CliText {
    ASK_NAME("Insert your nickname:"),
    ASK_NUMPLAYERS("Insert number of players:"),
    BAD_NAME(Color.RED.escape("Nickname format is not valid")),
    BAD_PLAYERNAME(Color.RED.escape("There is no player with nickname \"%s\"")),
    BAD_NUMPLAYERS_INT(Color.RED.escape("Number of players must be between 2 and 3:")),
    BAD_NUMPLAYERS_STRING(Color.RED.escape("I can only understand base-10 numbers... sorry.")),
    CORRECT_SIGNUP_WAIT(Color.GREEN.escape("Correct signup... wait for the other players")),
    CORRECT_SIGNUP_LAST(Color.GREEN.escape("Correct signup. Starting new game.")),
    YOUR_TURN(Color.YELLOW.escape("Your turn to play.")),
    WAIT_TURN(Color.YELLOW.escape("%s's turn to play. Wait...")),
    ASK_CHALLCARD_FIRST("Chose one card from the deck %s:"),
    ASK_CHALLCARD_MORE("Chose another card from the deck %s:"),
    OK_CHALLCARD1(Color.YELLOW.escape("%s, nice pick.")),
    OK_CHALLCARD2(Color.YELLOW.escape("%s, good choice.")),
    OK_CHALLCARD3(Color.YELLOW.escape("Oh, %s... well chosen.")),
    //BAD_CHALLCARD(Color.RED.escape("Invalid card name, chose from %s:")),
    ASK_CARD("Chose one of the cards selected by the Challenger %s:"),
    BAD_CARD(Color.RED.escape("Invalid card name, chose one of %s:")),
    SET_CARD_OTHER(Color.YELLOW.escape("Card %s set for player %s.")),
    SET_CARD_OWN(Color.YELLOW.escape("Card %s set for yourself.")),
    ASK_FIRSTPLAYER("Chose the Start Player [%s]:"),
    INVALID_NICKNAME(Color.RED.escape("Invalid nickName")),
    INVALID_NUMPLAYERS(Color.RED.escape("Incorrect num of players")),
    GAME_ALREADY_START(Color.RED.escape("Game already start, wait the end of the game...")),
    ENTER_COMMAND("Enter command:"),
    YOUR_TURN_COMMAND("Your turn to play. Enter command:"),
    BAD_COMMAND(Color.RED.escape("Not a valid command.")),
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
