package it.polimi.ingsw.client.cli;

import java.util.List;

public enum CliText {
    ASK_NAME("Insert your nickname:"),
    ASK_NUMPLAYERS("Insert number of players:"),
    BAD_NAME(Color.RED.escape("Nickname format is not valid")),
    BAD_NUMPLAYERS(Color.RED.escape("Insert number of players:")),
    CORRECT_SIGNUP_WAIT(Color.GREEN.escape("Correct signup... wait for the other players")),
    CORRECT_SIGNUP_LAST(Color.GREEN.escape("Correct signup. Starting new game.")),
    YOUR_TURN(Color.YELLOW.escape("Your turn to play.")),
    WAIT_TURN(Color.YELLOW.escape("%s's turn to play. Wait...")),
    YOUR_TURN_COMMAND("Your turn to play. Enter command:"),
    ASK_CHALLCARD("Chose one card from the deck: %s"),
    INVALID_NICKNAME(Color.RED.escape("Invalid nickName")),
    INVALID_NUMPLAYERS(Color.RED.escape("Incorrect num of players")),
    ENTER_COMMAND("Enter command:"),
    ;


    CliText(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public String toString(Object ... args) {
        return String.format(toPrompt(), args);
    }
    public String toPrompt(){
        return "\r\n"+ text + "\r\n> ";
    }
    public String toPrompt(Object ... args){
        return String.format(toPrompt(), args);
    }

    private final String text;
}
