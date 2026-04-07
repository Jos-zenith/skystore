package com.skystore.automation.pages;

import com.skystore.automation.core.AppConfig;
import com.skystore.automation.core.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage() {
        this.driver = TestContext.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get(AppConfig.baseUrl());
    }

    public void login(String email, String password) {
        open();
        driver.findElement(By.id("login-email")).clear();
        driver.findElement(By.id("login-email")).sendKeys(email);
        driver.findElement(By.id("login-password")).clear();
        driver.findElement(By.id("login-password")).sendKeys(password);
        driver.findElement(By.id("login-submit")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("login-screen")));
    }
}
