package Logger;

import java.util.concurrent.BlockingDeque;

public class Logger {
    private BlockingDeque
    private static Logger instance;
    private Logger(){

    }

    public static Logger getLoggerInstance(){
        if(instance == null){
            synchronized (Logger.class){
                if(instance == null){
                    instance = new Logger();
                }
            }
        }
        return instance;
    }
}
