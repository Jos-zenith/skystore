package com.skystore.automation.pages;

import org.openqa.selenium.By;

public class NavigationSidebar extends BasePage {

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
