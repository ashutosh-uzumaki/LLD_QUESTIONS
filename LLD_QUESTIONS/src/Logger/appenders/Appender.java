package Logger.appenders;

import Logger.models.LogMessage;

public interface Appender {
    void append(LogMessage logMessage);
}
