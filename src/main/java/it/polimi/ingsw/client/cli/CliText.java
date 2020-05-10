package it.polimi.ingsw.client.cli;

public enum CliText {
    ASK_NAME("Insert your nickname:"),
    BAD_NAME(Color.RED.escape("Nickname format is not valid")),
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
