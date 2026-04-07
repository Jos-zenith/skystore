package com.skystore.automation.core;

public final class AppConfig {
    private AppConfig() {
    }

    public static String baseUrl() {
        return firstNonBlank(
            System.getProperty("app.baseUrl"),
            System.getenv("APP_BASE_URL"),
            "http://localhost:3000"
        );
    }

    public static String browser() {
        return firstNonBlank(
            System.getProperty("browser"),
            System.getenv("BROWSER"),
            "chrome"
        );
    }

    public static boolean headless() {
        return Boolean.parseBoolean(firstNonBlank(
            System.getProperty("headless"),
            System.getenv("HEADLESS"),
            "true"
        ));
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return "";
    }
}
