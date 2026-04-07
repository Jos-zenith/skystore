package com.skystore.automation.bdd.hooks;

import com.skystore.automation.core.DriverFactory;
import com.skystore.automation.core.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void setUp() {
        TestContext.setDriver(DriverFactory.createDriver());
    }

    @After
    public void tearDown() {
        TestContext.clear();
    }
}
