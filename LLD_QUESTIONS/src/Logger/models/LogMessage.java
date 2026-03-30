package Logger.models;

import Logger.enums.LogLevel;

import java.time.LocalDateTime;

public final class LogMessage {
    private final String message;
    private final LogLevel logLevel;
    private final LocalDateTime timeStamp;

    public LogMessage(String message, LogLevel logLevel, LocalDateTime timeStamp){
        this.message = message;
        this.logLevel = logLevel;
        this.timeStamp = timeStamp;
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
