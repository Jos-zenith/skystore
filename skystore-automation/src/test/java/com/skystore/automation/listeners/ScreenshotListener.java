package com.skystore.automation.listeners;

import com.skystore.automation.core.TestContext;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class ScreenshotListener implements ITestListener {
    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = TestContext.getDriver();
        if (driver instanceof TakesScreenshot screenshot) {
            byte[] screen = screenshot.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Screenshot on failure", new ByteArrayInputStream(screen));
        }
    }
}