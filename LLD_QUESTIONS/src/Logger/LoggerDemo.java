package Logger;

import Logger.appenders.ConsoleAppender;
import Logger.appenders.FileAppender;
import Logger.enums.LogLevel;

public class LoggerDemo {
    public static void main(String[] args) {
        // 1. Get Singleton Logger Instance
        Logger logger = Logger.getLoggerInstance();

        // 2. Add Appenders (Multiple Destinations)
        logger.addAppender(new ConsoleAppender());
        logger.addAppender(new FileAppender("logs.txt"));

        // 3. Test Basic Logging
        logger.log(LogLevel.INFO, "Application started");
        logger.log(LogLevel.DEBUG, "Debug message (visible if threshold <= DEBUG)");
        logger.log(LogLevel.ERROR, "Something went wrong!");

        // 4. Test Multi-threaded Logging (Concurrency Test)
        System.out.println("Starting 5 concurrent threads...");
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            new Thread(() -> {
                logger.log(LogLevel.INFO, "Log from thread-" + threadId);
            }, "Worker-" + i).start();
        }

        // 5. Wait for async logs to flush (demo only)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("✅ Logging complete. Check logs.txt for file output.");

        // 6. Test Level Filtering (Set threshold to WARN, DEBUG/INFO should be ignored)
        // Note: To change threshold dynamically, you'd add a setter method (optional enhancement)
    }
}