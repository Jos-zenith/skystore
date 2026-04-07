# SkyStore Admin Portal

SkyStore is a cloud-style admin console demo with a full Java automation suite.

It includes:

- Node.js + Express web app for login, inventory CRUD, logs, and settings
- Selenium + TestNG Data-Driven tests (CSV and Excel)
- Selenium + TestNG Page Object Model tests
- Cucumber + TestNG BDD tests

## Project Structure

- `skystore-app/`: Web application (Node.js, Express, static frontend)
- `skystore-automation/`: Java Maven automation project
- `.github/workflows/test.yml`: CI pipeline for app + automation profiles


## 1. Run the App

```bash
cd skystore-app
npm install
npm start
```

App URL: `http://localhost:3000`

## 2. Configure Environment Variables

Use `skystore-app/.env.example` as the template.

Key variables:

- `APP_PORT`: app port (default `3000`)
- `MONGODB_URI`: Mongo connection string (leave empty to use JSON file mode)
- `MONGODB_DB`: Mongo database name
- `DATABASE_FILE`: JSON storage file path when Mongo is not used
- `SESSION_USER`, `SESSION_PASSWORD`: demo login credentials

For cloud MongoDB (Atlas), use an SRV URI format:

`mongodb+srv://<username>:<password>@<cluster-url>/skystore?retryWrites=true&w=majority`

## 3. Run Automation Profiles

```bash
cd skystore-automation
mvn test -PDataDriven
mvn test -PPOM
mvn test -PBDD
```

Optional base URL override:

```bash
mvn test -PPOM -Dapp.baseUrl=https://your-deployment.example.com
```

## Automation Highlights

- Excel fixture included at `skystore-automation/src/test/resources/data/products.xlsx`
- Shared `BasePage` for reusable driver/wait setup
- Negative-path coverage:
	- invalid login
	- duplicate SKU
	- empty required field
- Expanded BDD feature scenarios for edit/delete/bulk-upload/login failure
- Screenshot-on-failure listener with Allure attachment support

## Reporting

Allure TestNG dependency is configured in `skystore-automation/pom.xml`.

TestNG listener registration is in:

- `skystore-automation/src/test/resources/testng.xml`
- Test classes via `@Listeners`

## CI

GitHub Actions workflow (`.github/workflows/test.yml`) runs:

1. app install and startup
2. `DataDriven` profile
3. `POM` profile
4. `BDD` profile

## Cloud Deployment Note

You can deploy `skystore-app` using the provided Dockerfile and run automation against the deployed URL by setting `APP_BASE_URL` (or `-Dapp.baseUrl=...`).
