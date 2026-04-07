package com.skystore.automation.datadriven;

import com.skystore.automation.core.DriverFactory;
import com.skystore.automation.core.Product;
import com.skystore.automation.core.TestContext;
import com.skystore.automation.pages.DashboardPage;
import com.skystore.automation.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataDrivenProductTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.createDriver();
        TestContext.setDriver(driver);
        loginPage = new LoginPage();
        dashboardPage = new DashboardPage();
        loginPage.login("admin@skystore.io", "Password123!");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        TestContext.clear();
    }

    @Test(dataProvider = "products")
    public void shouldAddProductsFromDataSet(Product product) {
        if (dashboardPage.isProductVisible(product.getSku())) {
            dashboardPage.deleteProduct(product.getSku());
        }

        dashboardPage.addProduct(product);
        Assert.assertTrue(dashboardPage.toastText().contains("saved"));
        Assert.assertTrue(dashboardPage.isProductVisible(product.getSku()));

        dashboardPage.searchGlobal(product.getSku());
        dashboardPage.goToInventory();
        Assert.assertTrue(dashboardPage.isProductVisible(product.getSku()));
        Assert.assertEquals(dashboardPage.productCount(), 1);
        dashboardPage.clearGlobalSearch();
    }

    @DataProvider(name = "products")
    public Object[][] products() {
        return ProductDataProvider.loadProducts();
    }
}
