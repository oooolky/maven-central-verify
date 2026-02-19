package com.cs6650.client;

import io.swagger.client.model.Product;
import java.util.Random;

/**
 * Generates random products
 *
 * Assignment requirement:
 * productID - unsigned 32-bit integer between 1 and 4,294,967,295 inclusive, ie zero is disallowed
 * SKU - a string of 10 random characters from the set {A-Z0-9}. No whitespace, no punctuation, all upper case
 * Manufacturer - a string of words (you can use just the Latin-1 subset of Unicode or include other languages)
 * categoryID - integer between 1 and 100_000 inclusive
 * Weight - integer between 1,000 and 10,000 inclusive. Weight in grams
 * someOtherID - unsigned 32 integer, but zero is disallowed
 */
public class ProductGenerator {

    private static final String SKU_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SKU_LENGTH = 10;

    private static final String[] MANUFACTURERS = {
            "Acme Corp", "TechGiant Inc", "Global Widgets", "MegaStore Ltd",
            "Prime Electronics", "Quality Goods Co", "Best Products", "ValueMax",
            "Superior Items", "TopNotch Manufacturing"
    };

    private final Random random;

    public ProductGenerator() {
        this.random = new Random();
    }

    /**
     * Generates random product with valid field values
     *
     * @return a new Product instance with randomly generated data
     */
    public Product generateRandomProduct() {
        Product product = new Product();

        // productId: 1 to Integer.MAX_VALUE (using int range for simplicity)
        product.setProductId(random.nextInt(Integer.MAX_VALUE - 1) + 1);

        // sku: 10 random characters from A-Z and 0-9
        product.setSku(generateRandomSku());

        // manufacturer: random selection from predefined list
        product.setManufacturer(MANUFACTURERS[random.nextInt(MANUFACTURERS.length)]);

        // categoryId: 1 to 100,000
        product.setCategoryId(random.nextInt(100000) + 1);

        // weight: 1,000 to 10,000 grams
        product.setWeight(random.nextInt(9001) + 1000);

        // someOtherId: 1 to Integer.MAX_VALUE
        product.setSomeOtherId(random.nextInt(Integer.MAX_VALUE - 1) + 1);

        return product;
    }

    /**
     * Generates random SKU
     */
    private String generateRandomSku() {
        StringBuilder sku = new StringBuilder(SKU_LENGTH);
        for (int i = 0; i < SKU_LENGTH; i++) {
            int index = random.nextInt(SKU_CHARACTERS.length());
            sku.append(SKU_CHARACTERS.charAt(index));
        }
        return sku.toString();
    }
}