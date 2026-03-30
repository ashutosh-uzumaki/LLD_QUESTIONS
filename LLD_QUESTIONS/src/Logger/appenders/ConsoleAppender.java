package Logger.appenders;

import Logger.models.LogMessage;

public class ConsoleAppender implements Appender{
    @Override
    public void append(LogMessage message){
        System.out.println(message.getTimeStamp()+" "+message.getLogLevel()+" "+message.getMessage());
    }
}
