package com.skystore.automation.pom;

import com.skystore.automation.core.DriverFactory;
import com.skystore.automation.core.Product;
import com.skystore.automation.core.TestContext;
import com.skystore.automation.pages.DashboardPage;
import com.skystore.automation.pages.LoginPage;
import com.skystore.automation.pages.NavigationSidebar;
import com.skystore.automation.pages.SettingsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

@Listeners({com.skystore.automation.listeners.ScreenshotListener.class})
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
    @Description("Covers the core inventory lifecycle through the page objects.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Inventory CRUD")
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
    @Description("Validates cloud-style navigation and bulk upload entry points.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigation smoke")
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

    @Test
    @Description("Rejects invalid credentials without leaving the login screen.")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Authentication negative path")
    public void shouldRejectInvalidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.loginExpectingFailure("bad@skystore.io", "wrong-password");

        Assert.assertTrue(loginPage.toastText().contains("Invalid credentials"));
        Assert.assertTrue(loginPage.isLoginScreenVisible());
    }

    @Test
    @Description("Handles duplicate SKU submissions with a visible error toast.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Inventory validation")
    public void shouldHandleDuplicateSkuGracefully() {
        String sku = "SKU-DUP-" + System.currentTimeMillis();
        Product product = new Product("Duplicate SKU Item", 39.95, sku, "Testing");

        if (dashboardPage.isProductVisible(sku)) {
            dashboardPage.deleteProduct(sku);
        }

        dashboardPage.addProduct(product);
        Assert.assertTrue(dashboardPage.toastText().contains("saved"));

        dashboardPage.addDuplicateProduct(product);
        Assert.assertTrue(dashboardPage.toastText().contains("SKU already exists"));

        dashboardPage.closeProductModal();
        dashboardPage.deleteProduct(sku);
    }

    @Test
    @Description("Prevents an incomplete product form from being submitted.")
    @Severity(SeverityLevel.NORMAL)
    @Story("Inventory validation")
    public void shouldBlockEmptyRequiredFields() {
        Product incompleteProduct = new Product("Incomplete Item", 19.99, "", "Testing");

        dashboardPage.addProductWithMissingField(incompleteProduct, "sku");

        Assert.assertTrue(dashboardPage.toastText().contains("Missing required product fields"));
        Assert.assertTrue(dashboardPage.isProductModalVisible());
        dashboardPage.closeProductModal();
    }
}
