package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;
import it.polimi.ingsw.client.gui.SceneEvent;
import it.polimi.ingsw.server.message.ConnectionMessage;

/**
 * Class which implements ClientConnectionEventVisitor
 */
public class CliClientConnectionEventVisitor implements ClientConnectionEventVisitor {
    private final Cli cli;
    private CliModel cliModel;
    public CliClientConnectionEventVisitor(Cli cli, CliModel cliModel) {
        super();
        this.cli = cli;
        this.cliModel=cliModel;
    }

    /**
     * Function which after a ClientConnectionEvent, based on ClientConnectionEvent.Reason
     * prints a message
     * @param evt
     */
    @Override
    public void visit(ClientConnectionEvent evt) {

        if(evt.getReason().equals(ClientConnectionEvent.Reason.ERROR_ON_THE_SOCKET)){
            cli.print(System.lineSeparator() + Color.RED_UNDERLINED.escape("Errore nella connessione con il server."));
            cli.shutdown();
        }else if(evt.getReason().equals(ClientConnectionEvent.Reason.DISCONNECTION_TOO_MANY_PLAYERS)) {
            cli.print(System.lineSeparator() + Color.RED_UNDERLINED.escape("You have been kicked from the game, because there are too many players."));
            cli.shutdown();
        }
        else{
            if(!cliModel.getEndGame()) {
                cli.print(System.lineSeparator() + Color.RED_UNDERLINED.escape("Game over, player disconnected..."));
                cli.shutdown();
            }
        }
        cli.shutdown();
    }
}
