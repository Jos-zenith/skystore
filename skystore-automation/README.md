# SkyStore Automation

Java Selenium automation suite for the SkyStore Admin Portal.

## Tech Stack

- Selenium WebDriver
- TestNG
- Cucumber (BDD profile)
- Apache POI (Excel data)
- Allure TestNG integration

## Test Profiles

- `DataDriven`: dataset-driven inventory tests
- `POM`: page object model flow and negative-path tests
- `BDD`: Cucumber feature execution with TestNG runner

## Run Commands

```bash
mvn test -PDataDriven
mvn test -PPOM
mvn test -PBDD
```

## Runtime Configuration

- `APP_BASE_URL` environment variable
- or JVM system property: `-Dapp.baseUrl=...`

Examples:

```bash
set APP_BASE_URL=http://localhost:3000
mvn test -PPOM
```

```bash
mvn test -PBDD -Dapp.baseUrl=https://your-deployment.example.com
```

## Data-Driven Inputs

- CSV: `src/test/resources/data/products.csv`
- Excel: `src/test/resources/data/products.xlsx`

The `DataDriven` profile sets `productExcelPath` to the repository Excel fixture.

## Reporting and Diagnostics

- Allure TestNG dependency configured in `pom.xml`
- Screenshot-on-failure listener:
	- `src/test/java/com/skystore/automation/listeners/ScreenshotListener.java`
- TestNG suite listener registration:
	- `src/test/resources/testng.xml`
