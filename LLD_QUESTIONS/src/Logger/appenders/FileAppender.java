package Logger.appenders;

import Logger.models.LogMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class FileAppender implements Appender{
    private final String filePath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileAppender(String filePath){
        this.filePath = filePath;
    }

    @Override
    public void append(LogMessage logMessage) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))){
            String logLine = String.format("%s %s %s%n",
                    logMessage.getTimeStamp().format(formatter),
                    logMessage.getLogLevel(),
                    logMessage.getMessage());
            writer.write(logLine);
            writer.flush();
        }catch(IOException exception){
            System.err.println("Error occured: "+exception.getMessage());
        }
    }
}
