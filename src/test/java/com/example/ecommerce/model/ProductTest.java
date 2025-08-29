package com.example.ecommerce.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product();
        assertNotNull(product);
    }

    @Test
    void testIdGetterAndSetter() {
        Product product = new Product();
        Long id = 1L;
        product.setId(id);
        assertEquals(id, product.getId());
    }

    @Test
    void testNameGetterAndSetter() {
        Product product = new Product();
        String name = "Test Product";
        product.setName(name);
        assertEquals(name, product.getName());
    }

    @Test
    void testDescriptionGetterAndSetter() {
        Product product = new Product();
        String description = "Test Description";
        product.setDescription(description);
        assertEquals(description, product.getDescription());
    }

    @Test
    void testPriceGetterAndSetter() {
        Product product = new Product();
        double price = 99.99;
        product.setPrice(price);
        assertEquals(price, product.getPrice(), 0.001);
    }

    @Test
    void testPriceWithZero() {
        Product product = new Product();
        product.setPrice(0.0);
        assertEquals(0.0, product.getPrice(), 0.001);
    }

    @Test
    void testPriceWithNegativeValue() {
        Product product = new Product();
        product.setPrice(-10.0);
        assertEquals(-10.0, product.getPrice(), 0.001);
    }

    @Test
    void testPriceWithLargeValue() {
        Product product = new Product();
        double largePrice = 999999.99;
        product.setPrice(largePrice);
        assertEquals(largePrice, product.getPrice(), 0.001);
    }

    @Test
    void testNullName() {
        Product product = new Product();
        product.setName(null);
        assertNull(product.getName());
    }

    @Test
    void testEmptyName() {
        Product product = new Product();
        product.setName("");
        assertEquals("", product.getName());
    }

    @Test
    void testNullDescription() {
        Product product = new Product();
        product.setDescription(null);
        assertNull(product.getDescription());
    }

    @Test
    void testEmptyDescription() {
        Product product = new Product();
        product.setDescription("");
        assertEquals("", product.getDescription());
    }

    @Test
    void testNullId() {
        Product product = new Product();
        product.setId(null);
        assertNull(product.getId());
    }

    @Test
    void testCompleteProductSetup() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("High-performance laptop");
        product.setPrice(1299.99);

        assertEquals(1L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals("High-performance laptop", product.getDescription());
        assertEquals(1299.99, product.getPrice(), 0.001);
    }
}
