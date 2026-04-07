package com.skystore.automation.pom;

import com.skystore.automation.core.DriverFactory;
import com.skystore.automation.core.Product;
import com.skystore.automation.core.TestContext;
import com.skystore.automation.pages.DashboardPage;
import com.skystore.automation.pages.LoginPage;
import com.skystore.automation.pages.NavigationSidebar;
import com.skystore.automation.pages.SettingsPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PomInventoryTest {
    private WebDriver driver;
    private DashboardPage dashboardPage;
    private SettingsPage settingsPage;
    private NavigationSidebar navigationSidebar;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver();
        TestContext.setDriver(driver);
        new LoginPage().login("admin@skystore.io", "Password123!");
        dashboardPage = new DashboardPage();
        settingsPage = new SettingsPage();
        navigationSidebar = new NavigationSidebar();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        TestContext.clear();
    }

    @Test
    public void shouldCreateEditAndDeleteProductThroughPages() {
        String sku = "SKU-POM-" + System.currentTimeMillis();
        Product created = new Product("POM Widget", 99.95, sku, "Accessories");
        Product updated = new Product("POM Widget Plus", 109.95, sku, "Accessories");

        dashboardPage.addProduct(created);
        Assert.assertTrue(dashboardPage.isProductVisible(sku));
        Assert.assertTrue(dashboardPage.toastText().contains("saved"));

        dashboardPage.editProduct(updated);
        Assert.assertTrue(dashboardPage.toastText().contains("updated"));

        Assert.assertTrue(dashboardPage.productSkus().contains(sku));
        Assert.assertTrue(settingsPage.cloudStatus().length() > 0);

        dashboardPage.deleteProduct(sku);
        Assert.assertFalse(dashboardPage.isProductVisible(sku));
    }

    @Test
    public void shouldSupportAwsStyleNavigationAndTopBarActions() {
        navigationSidebar.openBulkUpload();
        Assert.assertEquals(dashboardPage.pageTitle(), "Bulk Upload");
        Assert.assertTrue(dashboardPage.breadcrumbText().contains("Bulk Upload"));

        navigationSidebar.openSettings();
        Assert.assertEquals(dashboardPage.pageTitle(), "Cloud Settings");
        Assert.assertTrue(dashboardPage.breadcrumbText().contains("Cloud Configuration"));
        Assert.assertTrue(settingsPage.cloudStatus().length() > 0);

        dashboardPage.switchRegion("eu-west-1");
        Assert.assertTrue(dashboardPage.toastText().contains("Region switched"));
        Assert.assertEquals(dashboardPage.selectedRegion(), "eu-west-1");
    }
}
