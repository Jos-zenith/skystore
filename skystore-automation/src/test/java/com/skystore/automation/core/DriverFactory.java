package com.skystore.automation.core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = AppConfig.browser().toLowerCase();
        boolean headless = AppConfig.headless();

        if (browser.contains("firefox")) {
            return createFirefox(headless);
        }

        return createChrome(headless);
    }

    private static WebDriver createChrome(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1200");
        if (headless) {
            options.addArguments("--headless=new");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefox(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }
}
