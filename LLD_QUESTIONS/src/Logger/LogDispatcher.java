package Logger;

import Logger.appenders.Appender;
import Logger.models.LogMessage;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LogDispatcher implements Runnable{
    private final BlockingQueue<LogMessage> queue;
    private final List<Appender> appenderList;

    public LogDispatcher(BlockingQueue queue, List<Appender> appenderList){
        this.queue = queue;
        this.appenderList = appenderList;
    }

    @Override
    public void run(){
        while(true){
            try {
                LogMessage logMessage = queue.take();
                for(Appender appender: appenderList){
                    appender.append(logMessage);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
