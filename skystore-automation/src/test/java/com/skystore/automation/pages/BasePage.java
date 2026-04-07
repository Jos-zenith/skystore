package com.skystore.automation.pages;

import com.skystore.automation.core.AppConfig;
import com.skystore.automation.core.TestContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = TestContext.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(AppConfig.pageTimeoutSeconds()));
    }
}