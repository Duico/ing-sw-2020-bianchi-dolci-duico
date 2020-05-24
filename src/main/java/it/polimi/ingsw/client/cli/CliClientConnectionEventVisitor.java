package it.polimi.ingsw.client.cli;

import it.polimi.ingsw.client.ClientConnectionEventVisitor;
import it.polimi.ingsw.client.event.ClientConnectionEvent;

public class CliClientConnectionEventVisitor extends ClientConnectionEventVisitor {
    private final Cli cli;
    public CliClientConnectionEventVisitor(Cli cli) {
        super();
        this.cli = cli;
    }

    @Override
    public void visit(ClientConnectionEvent evt) {
        cli.print(Color.RED_UNDERLINED.escape(evt.getReason().toString()));
    }
}
