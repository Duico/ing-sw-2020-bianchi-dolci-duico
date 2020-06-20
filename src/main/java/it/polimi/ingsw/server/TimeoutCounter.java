package it.polimi.ingsw.server;

import java.util.TimerTask;

public class TimeoutCounter extends TimerTask {
    TimeOutCheckerInterface timeOutChecker;

    public TimeoutCounter(TimeOutCheckerInterface timeOutChecker){
        this.timeOutChecker = timeOutChecker;
    }
    public static int i = 0;
    public void run()
    {
        Boolean stop = timeOutChecker.check();
        if (stop) {
            this.cancel();
        }
    }
}
