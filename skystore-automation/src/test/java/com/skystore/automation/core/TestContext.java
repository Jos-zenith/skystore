package com.skystore.automation.core;

import org.openqa.selenium.WebDriver;

public final class TestContext {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private TestContext() {
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static void clear() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
        }
        DRIVER.remove();
    }
}
