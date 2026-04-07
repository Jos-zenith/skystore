package com.skystore.automation.pages;

import com.skystore.automation.core.AppConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    public void open() {
        driver.get(AppConfig.baseUrl());
    }

    public void login(String email, String password) {
        open();
        submitCredentials(email, password);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("login-screen")));
    }

    public void loginExpectingFailure(String email, String password) {
        open();
        submitCredentials(email, password);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast")));
    }

    public boolean isLoginScreenVisible() {
        return driver.findElement(By.id("login-screen")).isDisplayed();
    }

    public String toastText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast"))).getText();
    }

    private void submitCredentials(String email, String password) {
        driver.findElement(By.id("login-email")).clear();
        driver.findElement(By.id("login-email")).sendKeys(email);
        driver.findElement(By.id("login-password")).clear();
        driver.findElement(By.id("login-password")).sendKeys(password);
        driver.findElement(By.id("login-submit")).click();
    }
}
