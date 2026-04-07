package com.skystore.automation.datadriven;

import com.skystore.automation.core.Product;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class ExcelProductReader {
    private ExcelProductReader() {
    }

    public static List<Product> read(Path workbookPath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(workbookPath); XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            List<Product> products = new ArrayList<>();
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                for (Row row : workbook.getSheetAt(sheetIndex)) {
                    if (row.getRowNum() == 0) {
                        continue;
                    }
                    String name = readText(row.getCell(0));
                    String priceText = readText(row.getCell(1));
                    String sku = readText(row.getCell(2));
                    String category = readText(row.getCell(3));
                    if (!name.isBlank() && !sku.isBlank()) {
                        products.add(new Product(name, Double.parseDouble(priceText), sku, category));
                    }
                }
            }
            return products;
        }
    }

    private static String readText(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
