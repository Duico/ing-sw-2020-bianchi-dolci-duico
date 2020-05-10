package it.polimi.ingsw.client.cli;

public enum CliText {
    ASK_NAME("Insert your nickname:"),
    ASK_NUMPLAYERS("Insert number of players:"),
    BAD_NAME(Color.RED.escape("Nickname format is not valid")),
    BAD_NUMPLAYERS(Color.RED.escape("Insert number of players:")),
    CORRECT_SIGNUP(Color.GREEN.escape("Correct signup... wait")),
    ENTER_COMMAND("Enter command:");

    CliText(String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
    public String toPrompt(){
        return "\r\n"+ text + "\r\n> ";
    }

    private final String text;
}
