package com.skystore.automation.pages;

import com.skystore.automation.core.Product;
import com.skystore.automation.core.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DashboardPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public DashboardPage() {
        this.driver = TestContext.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void goToInventory() {
        driver.findElement(By.cssSelector("[data-testid='inventory-nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inventory-view")));
    }

    public void goToBulkUpload() {
        driver.findElement(By.cssSelector("[data-testid='bulk-upload-nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bulk-upload-view")));
    }

    public void goToLogs() {
        driver.findElement(By.cssSelector("[data-testid='logs-nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logs-view")));
    }

    public void goToSettings() {
        driver.findElement(By.cssSelector("[data-testid='settings-nav-link']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("settings-view")));
    }

    public void openAddProductModal() {
        goToInventory();
        driver.findElement(By.id("add-product-btn")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product-modal")));
    }

    public void addProduct(Product product) {
        openAddProductModal();
        fillProductForm(product, false);
        submitProduct();
    }

    public void editProduct(Product product) {
        goToInventory();
        driver.findElement(By.cssSelector("[data-action='edit'][data-sku='" + product.getSku() + "']")).click();
        fillProductForm(product, true);
        submitProduct();
    }

    public void deleteProduct(String sku) {
        goToInventory();
        driver.findElement(By.cssSelector("[data-action='delete'][data-sku='" + sku + "']")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-testid='product-row-" + sku + "']")));
    }

    public boolean isProductVisible(String sku) {
        return !driver.findElements(By.cssSelector("[data-testid='product-row-" + sku + "']")).isEmpty();
    }

    public int productCount() {
        return driver.findElements(By.cssSelector("[data-testid^='product-row-']")).size();
    }

    public String toastText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast"))).getText();
    }

    public String cloudStatus() {
        return driver.findElement(By.id("cloud-status-badge")).getText();
    }

    public String pageTitle() {
        return driver.findElement(By.id("page-title")).getText();
    }

    public String breadcrumbText() {
        return driver.findElement(By.id("breadcrumb-row")).getText();
    }

    public void searchGlobal(String term) {
        WebElement searchInput = driver.findElement(By.id("global-search-input"));
        searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        searchInput.sendKeys(Keys.DELETE);
        searchInput.sendKeys(term);
    }

    public void clearGlobalSearch() {
        WebElement searchInput = driver.findElement(By.id("global-search-input"));
        searchInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        searchInput.sendKeys(Keys.DELETE);
    }

    public void switchRegion(String regionName) {
        Select select = new Select(driver.findElement(By.id("region-selector")));
        select.selectByVisibleText(regionName);
    }

    public String selectedRegion() {
        Select select = new Select(driver.findElement(By.id("region-selector")));
        return select.getFirstSelectedOption().getText();
    }

    public List<String> productSkus() {
        List<String> skus = new ArrayList<>();
        for (WebElement row : driver.findElements(By.cssSelector("[data-testid^='product-row-']"))) {
            skus.add(row.getDomAttribute("data-testid").replace("product-row-", ""));
        }
        return skus;
    }

    private void fillProductForm(Product product, boolean preserveSku) {
        WebElement name = driver.findElement(By.id("product-name-input"));
        WebElement sku = driver.findElement(By.id("product-sku-input"));
        WebElement category = driver.findElement(By.id("product-category-input"));
        WebElement price = driver.findElement(By.id("product-price-input"));

        name.clear();
        name.sendKeys(product.getName());
        if (!preserveSku) {
            sku.clear();
            sku.sendKeys(product.getSku());
        }
        category.clear();
        category.sendKeys(product.getCategory());
        price.clear();
        price.sendKeys(String.valueOf(product.getPrice()));
    }

    private void submitProduct() {
        driver.findElement(By.id("save-product-btn")).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("product-modal")));
    }
}
