package com.skystore.automation.datadriven;

import com.skystore.automation.core.Product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class ProductDataProvider {
    private ProductDataProvider() {
    }

    public static Object[][] loadProducts() {
        List<Product> products = loadProductList();
        Object[][] rows = new Object[products.size()][1];
        for (int index = 0; index < products.size(); index++) {
            rows[index][0] = products.get(index);
        }
        return rows;
    }

    private static List<Product> loadProductList() {
        String excelPath = System.getProperty("productExcelPath");
        if (excelPath != null && !excelPath.isBlank()) {
            Path workbookPath = Paths.get(excelPath);
            if (Files.exists(workbookPath)) {
                try {
                    return ExcelProductReader.read(workbookPath);
                } catch (IOException exception) {
                    throw new IllegalStateException("Unable to read Excel product data", exception);
                }
            }
        }

        try {
            return readCsvFromClasspath("data/products.csv");
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to read product data", exception);
        }
    }

    private static List<Product> readCsvFromClasspath(String resourcePath) throws IOException {
        InputStream inputStream = ProductDataProvider.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Missing classpath resource: " + resourcePath);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<Product> products = new ArrayList<>();
            String line;
            boolean header = true;
            while ((line = reader.readLine()) != null) {
                if (header) {
                    header = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                String[] columns = line.split(",");
                products.add(new Product(columns[0], Double.parseDouble(columns[1]), columns[2], columns[3]));
            }
            return products;
        }
    }
}
