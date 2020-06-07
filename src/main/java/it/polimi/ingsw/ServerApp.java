package it.polimi.ingsw;

import com.sun.javafx.application.ParametersImpl;
import it.polimi.ingsw.server.Server;
import javafx.application.Application;

import java.io.IOException;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class ServerApp
{
    public static void main( String[] args )
    {
        Server server;
        try {
            Application.Parameters params = new ParametersImpl(args);
            TcpParamsParser parser = new TcpParamsParser(params);
            Integer paramsPort = 0;
            try{
                paramsPort = parser.getPort();
            } catch (TcpParamsParser.IllegalParameterException e) {
                System.out.println("Missing or wrong port selected. Using random port.");
            }
            server = new Server(paramsPort);
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }
}
