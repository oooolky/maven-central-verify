package com.cs6650.productservice.model;

/**
 * Product class represents an item in the e-commerce system.
 *
 * Assignment requirement:
 * productID - unsigned 32-bit integer between 1 and 4,294,967,295 inclusive, ie zero is disallowed
 * SKU - a string of 10 random characters from the set {A-Z0-9}. No whitespace, no punctuation, all upper case
 * Manufacturer - a string of words (you can use just the Latin-1 subset of Unicode or include other languages)
 * categoryID - integer between 1 and 100_000 inclusive
 * Weight - integer between 1,000 and 10,000 inclusive. Weight in grams
 * someOtherID - unsigned 32 integer, but zero is disallowed
 */
public class Product {

    private Integer productId;
    private String sku;
    private String manufacturer;
    private Integer categoryId;
    private Integer weight;
    private Integer someOtherId;

    // Default constructor required for JSON deserialization
    public Product() {
    }

    // Getters and Setters
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSomeOtherId() {
        return someOtherId;
    }

    public void setSomeOtherId(Integer someOtherId) {
        this.someOtherId = someOtherId;
    }
}