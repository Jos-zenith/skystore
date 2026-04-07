package com.skystore.automation.bdd.steps;

import com.skystore.automation.core.Product;
import com.skystore.automation.pages.DashboardPage;
import com.skystore.automation.pages.LoginPage;
import com.skystore.automation.pages.NavigationSidebar;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class InventorySteps {
    private final LoginPage loginPage = new LoginPage();
    private final DashboardPage dashboardPage = new DashboardPage();
    private final NavigationSidebar navigationSidebar = new NavigationSidebar();
    private Product pendingProduct;

    @Given("the user is logged into the SkyStore Admin Portal")
    public void theUserIsLoggedIntoTheSkyStoreAdminPortal() {
        loginPage.login("admin@skystore.io", "Password123!");
    }

    @When("they enter product details for {string}")
    public void theyEnterProductDetailsFor(String productName) {
        pendingProduct = new Product(productName, 249.99, "SKU-CLOUD-PRO-BDD", "Infrastructure");
        if (dashboardPage.isProductVisible(pendingProduct.getSku())) {
            dashboardPage.deleteProduct(pendingProduct.getSku());
        }
        dashboardPage.addProduct(pendingProduct);
    }

    @Then("the item should be visible in the inventory list")
    public void theItemShouldBeVisibleInTheInventoryList() {
        Assert.assertTrue(dashboardPage.isProductVisible(pendingProduct.getSku()));
        Assert.assertTrue(dashboardPage.toastText().contains("saved"));
    }

    @When("they switch cloud region to {string}")
    public void theySwitchCloudRegionTo(String region) {
        dashboardPage.switchRegion(region);
        Assert.assertTrue(dashboardPage.toastText().contains("Region switched"));
    }

    @When("they open cloud settings from the sidebar")
    public void theyOpenCloudSettingsFromTheSidebar() {
        navigationSidebar.openSettings();
    }

    @Then("the breadcrumb should contain {string}")
    public void theBreadcrumbShouldContain(String breadcrumbText) {
        Assert.assertTrue(dashboardPage.breadcrumbText().contains(breadcrumbText));
    }

    @Then("the selected region should be {string}")
    public void theSelectedRegionShouldBe(String region) {
        Assert.assertEquals(dashboardPage.selectedRegion(), region);
    }
}
