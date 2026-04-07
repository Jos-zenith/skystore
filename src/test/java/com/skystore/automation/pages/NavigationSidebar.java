package com.skystore.automation.pages;

import com.skystore.automation.core.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class NavigationSidebar {
    private final WebDriver driver;

    public NavigationSidebar() {
        this.driver = TestContext.getDriver();
    }

    public void openDashboard() {
        driver.findElement(By.cssSelector("[data-testid='dashboard-nav-link']")).click();
    }

    public void openInventory() {
        driver.findElement(By.cssSelector("[data-testid='inventory-nav-link']")).click();
    }

    public void openBulkUpload() {
        driver.findElement(By.cssSelector("[data-testid='bulk-upload-nav-link']")).click();
    }

    public void openLogs() {
        driver.findElement(By.cssSelector("[data-testid='logs-nav-link']")).click();
    }

    public void openSettings() {
        driver.findElement(By.cssSelector("[data-testid='settings-nav-link']")).click();
    }
}
