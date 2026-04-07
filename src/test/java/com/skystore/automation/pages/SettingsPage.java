package com.skystore.automation.pages;

import com.skystore.automation.core.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SettingsPage {
    private final WebDriver driver;

    public SettingsPage() {
        this.driver = TestContext.getDriver();
    }

    public String cloudStatus() {
        return driver.findElement(By.id("settings-cloud-status")).getText();
    }

    public String databaseFile() {
        return driver.findElement(By.id("settings-database")).getText();
    }
}
