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
    private String updatedProductName;

    @Given("the user is logged into the SkyStore Admin Portal")
    public void theUserIsLoggedIntoTheSkyStoreAdminPortal() {
        loginPage.login("admin@skystore.io", "Password123!");
    }

    @Given("the user is on the SkyStore login screen")
    public void theUserIsOnTheSkyStoreLoginScreen() {
        loginPage.open();
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

    @When("they edit that product to {string}")
    public void theyEditThatProductTo(String updatedName) {
        updatedProductName = updatedName;
        Product updatedProduct = new Product(updatedName, 259.99, pendingProduct.getSku(), pendingProduct.getCategory());
        dashboardPage.editProduct(updatedProduct);
    }

    @Then("the item should reflect the updated name")
    public void theItemShouldReflectTheUpdatedName() {
        Assert.assertTrue(dashboardPage.productRowText(pendingProduct.getSku()).contains(updatedProductName));
        Assert.assertTrue(dashboardPage.toastText().contains("updated"));
    }

    @When("they delete that product")
    public void theyDeleteThatProduct() {
        dashboardPage.deleteProduct(pendingProduct.getSku());
    }

    @Then("the item should be removed from the inventory list")
    public void theItemShouldBeRemovedFromTheInventoryList() {
        Assert.assertFalse(dashboardPage.isProductVisible(pendingProduct.getSku()));
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

    @When("they attempt to sign in with invalid credentials")
    public void theyAttemptToSignInWithInvalidCredentials() {
        loginPage.loginExpectingFailure("invalid@skystore.io", "wrong-password");
    }

    @Then("the login error should be visible")
    public void theLoginErrorShouldBeVisible() {
        Assert.assertTrue(loginPage.toastText().contains("Invalid credentials"));
        Assert.assertTrue(loginPage.isLoginScreenVisible());
    }

    @When("they open the bulk upload view")
    public void theyOpenTheBulkUploadView() {
        navigationSidebar.openBulkUpload();
    }

    @Then("the bulk upload page should be visible")
    public void theBulkUploadPageShouldBeVisible() {
        Assert.assertEquals(dashboardPage.pageTitle(), "Bulk Upload");
        Assert.assertTrue(dashboardPage.breadcrumbText().contains("Bulk Upload"));
    }
}
