package Logger.models;

import Logger.enums.LogLevel;

import java.time.LocalDateTime;

public final class LogMessage {
    private final String message;
    private final LogLevel logLevel;
    private final LocalDateTime timeStamp;

    public LogMessage(String message, LogLevel logLevel){
        this.message = message;
        this.logLevel = logLevel;
        this.timeStamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
}
