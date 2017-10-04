package com.lockit;

public class Logger {
    public static void logError(Throwable t) {
        t.printStackTrace();
    }
}