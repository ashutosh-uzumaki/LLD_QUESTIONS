package Logger;

import Logger.appenders.Appender;
import Logger.enums.LogLevel;
import Logger.models.LogMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger {
    private final BlockingQueue<LogMessage> queue = new LinkedBlockingQueue<>();
    private static volatile Logger instance;
    private final LogLevel threshold = LogLevel.DEBUG;
    private final List<Appender> appenderList = new ArrayList<>();
    private Logger(){
        Thread dispatcher = new Thread(new LogDispatcher(queue, appenderList));
        dispatcher.setDaemon(true);
        dispatcher.start();
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

    public void log(LogLevel level, String logMessage){
        if(level.ordinal() < threshold.ordinal()){
            return;
        }

        try{
            queue.put(new LogMessage(logMessage, level));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void addAppender(Appender appender){
        appenderList.add(appender);
    }
}
