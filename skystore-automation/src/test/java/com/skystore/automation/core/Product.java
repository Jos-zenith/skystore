package com.skystore.automation.core;

public class Product {
    private final String name;
    private final double price;
    private final String sku;
    private final String category;

    public Product(String name, double price, String sku, String category) {
        this.name = name;
        this.price = price;
        this.sku = sku;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public String getCategory() {
        return category;
    }
}
