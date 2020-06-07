package it.polimi.ingsw;
import javafx.application.Application;

import java.util.Map;

public class TcpParamsParser {
    private Map<String, String> paramMap;
    public TcpParamsParser(Application.Parameters params) {
        this.paramMap = params.getNamed();
    }

    public String getIp() throws IllegalParameterException{
        String paramsIp = null;
        if (paramMap.containsKey("ip")) { //regexp
            paramsIp = paramMap.get("ip");
        } else if (paramMap.containsKey("addr")) {
            paramsIp = paramMap.get("addr");
        }
        if (paramsIp == null || !paramsIp.matches("^((25[0-5]|(2[0-4]|1[0-9]|[1-9]|)[0-9])(\\.(?!$)|$)){4}$")) {
            throw new IllegalParameterException();
        }
        return paramsIp;
    }

    public Integer getPort() throws IllegalParameterException{
        Integer paramsPort = null;
        if (paramMap.containsKey("port")) {
            paramsPort = Integer.parseInt(paramMap.get("port"));
        }
        if (paramsPort == null || paramsPort > 65535 || paramsPort < 1024) {
            throw new IllegalParameterException();
        }
        return paramsPort;
    }

    public class IllegalParameterException extends Exception {
    }
}
