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
    YOUR_TURN(Color.YELLOW_UNDERLINED.escape("Your turn to play.")),
    WAIT_TURN(Color.YELLOW_BOLD.escape("%s")+Color.YELLOW.escape("'s turn to play. Wait...")),
    WAIT_TURN_RED(Color.RED_BOLD.escape("%s")+Color.RED.escape("'s turn: commands are discarded. Wait...")),
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
    GAME_ALREADY_STARTED(Color.RED.escape("Game already started, wait the end of the game...")),
    ENTER_COMMAND("Enter command:"),
    YOUR_TURN_COMMAND("Your turn to play. Enter command:"),
    BAD_COMMAND(Color.RED.escape("Not a valid command.")),
    SUCCESSFUL_OPERATION(Color.YELLOW_BOLD.escape("Successful operation")),
    PLAYER_DISCONNECTED(Color.RED.escape("End game, player disconnected")),
    WORKERS_ALREADY_PLACE(Color.RED.escape("You have already placed all of your workers")),
    PLACE_DESTINATION_NOT_EMPTY(Color.RED.escape("Place destination is not empty")),
    NOT_CURRENT_WORKER(Color.RED.escape("You can't move/build with that worker")),
    NOT_ALLOWED_TO_MOVE(Color.RED.escape("You are not allowed to move")),
    NOT_ALLOWED_TO_BUILD(Color.RED.escape("You are not allowed to build")),
    BLOCKED_MOVE(Color.RED.escape("This movement is blocked")),
    NOT_FEASIBLE_MOVE(Color.RED.escape("Is not a feasible movement")),
    NOT_FEASIBLE_BUILD(Color.RED.escape("Is not a feasible build")),
    REQUIRED_MOVE(Color.RED.escape("Is required a movement before end turn")),
    REQUIRED_BUILD(Color.RED.escape("Is required a build before end turn")),
    REQUIRED_PLACE(Color.RED.escape("Place all the workers before end turn")),
    WINNER(Color.GREEN_UNDERLINED.escape("You are the winner, my best compliments")),
    LOSER(Color.RED_BOLD.escape("%s")+Color.RED.escape(" wins, end game")),
    LOSER_BLOCK(Color.RED.escape("You are blocked, end game for you")),
    ADVISE_LOSER_BLOCK(Color.RED_BOLD.escape("%s")+Color.RED.escape(" loses, end game for him")),
    UNDO_NOT_AVAILABLE(Color.RED.escape("Undo not available"))

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
