package com.skystore.automation.pages;

import org.openqa.selenium.By;

public class SettingsPage extends BasePage {

    public String cloudStatus() {
        return driver.findElement(By.id("settings-cloud-status")).getText();
    }

    public String databaseFile() {
        return driver.findElement(By.id("settings-database")).getText();
    }
}
