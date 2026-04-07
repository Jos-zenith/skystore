# SkyStore Admin Portal

A small cloud-ready product catalog app plus a Java Selenium automation suite organized into three mini-projects:

- Data-Driven testing with Selenium, TestNG, and Apache POI/CSV support
- Page Object Model testing with Selenium and TestNG
- BDD testing with Cucumber and TestNG

## Repository Layout

- `skystore-app/` - Node.js + Express dashboard app with a login screen, inventory CRUD, logs, and settings views
- `skystore-automation/` - Java Maven test suite for Data-Driven, POM, and BDD flows

## Quick Start

### App

```bash
cd skystore-app
npm install
npm start
```

Open `http://localhost:3000`.

To switch the app to MongoDB Atlas, set `MONGODB_URI` and `MONGODB_DB` in `skystore-app/.env` or your shell. If those values are not set, the app keeps using the local JSON file store.

### Automation

Set `APP_BASE_URL` if needed, then run one profile at a time:

```bash
cd skystore-automation
mvn test -P DataDriven
mvn test -P POM
mvn test -P BDD
```

## Cloud Notes

The app is environment-driven so you can point it at a cloud deployment by changing the base URL and backend data source. The UI already exposes stable `data-testid` attributes for Selenium.
