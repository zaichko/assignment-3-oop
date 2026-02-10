package kz.aitu.digitalcontent.patterns;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {
    private static LoggerService instance;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private LoggerService() {}

    public static synchronized LoggerService getInstance() {
        if (instance == null) {
            instance = new LoggerService();
        }
        return instance;
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println(String.format("[%s] %s: %s", timestamp, level, message));
    }
}