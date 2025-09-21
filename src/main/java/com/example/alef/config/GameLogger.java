package com.example.alef.config;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

public class GameLogger {

    private static final ZoneId MSK_ZONE = ZoneId.of("Europe/Moscow");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        String timestamp = ZonedDateTime.now(MSK_ZONE).format(FORMATTER);
        System.out.println("[" + timestamp + "] " + message);
    }

    public static void info(String message) {
        log("INFO: " + message);
    }

    public static void warn(String message) {
        log("WARN: " + message);
    }

    public static void error(String message) {
        log("ERROR: " + message);
    }
}

