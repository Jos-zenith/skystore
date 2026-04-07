package com.skystore.automation.bdd.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Listeners;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.skystore.automation.bdd.hooks",
        "com.skystore.automation.bdd.steps"
    },
    plugin = {
        "pretty",
        "summary"
    }
)
@Listeners({com.skystore.automation.listeners.ScreenshotListener.class})
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
