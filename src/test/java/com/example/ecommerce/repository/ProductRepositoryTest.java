package com.example.ecommerce.repository;

import com.example.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertEquals("Test Product", savedProduct.getName());
        assertEquals("Test Description", savedProduct.getDescription());
        assertEquals(99.99, savedProduct.getPrice(), 0.001);
    }

    @Test
    void testSaveProductWithNullFields() {
        Product product = new Product();
        product.setName(null);
        product.setDescription(null);
        product.setPrice(0.0);

        Product savedProduct = productRepository.save(product);

        assertNotNull(savedProduct.getId());
        assertNull(savedProduct.getName());
        assertNull(savedProduct.getDescription());
        assertEquals(0.0, savedProduct.getPrice(), 0.001);
    }

    @Test
    void testFindById_ExistingProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals("Test Product", foundProduct.get().getName());
        assertEquals("Test Description", foundProduct.get().getDescription());
        assertEquals(99.99, foundProduct.get().getPrice(), 0.001);
    }

    @Test
    void testFindById_NonExistingProduct() {
        Optional<Product> foundProduct = productRepository.findById(999L);

        assertFalse(foundProduct.isPresent());
    }

    @Test
    void testFindAll_EmptyList() {
        List<Product> products = productRepository.findAll();

        assertTrue(products.isEmpty());
    }

    @Test
    void testFindAll_WithProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);
        productRepository.save(product2);

        List<Product> products = productRepository.findAll();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> "Product 1".equals(p.getName())));
        assertTrue(products.stream().anyMatch(p -> "Product 2".equals(p.getName())));
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Original Name");
        product.setDescription("Original Description");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        savedProduct.setName("Updated Name");
        savedProduct.setDescription("Updated Description");
        savedProduct.setPrice(100.0);
        Product updatedProduct = productRepository.save(savedProduct);

        assertEquals(savedProduct.getId(), updatedProduct.getId());
        assertEquals("Updated Name", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(100.0, updatedProduct.getPrice(), 0.001);
    }

    @Test
    void testDeleteById_ExistingProduct() {
        Product product = new Product();
        product.setName("To Delete");
        product.setDescription("Will be deleted");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        productRepository.deleteById(savedProduct.getId());

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertFalse(deletedProduct.isPresent());
    }

    @Test
    void testDeleteById_NonExistingProduct() {
        // Should not throw exception
        productRepository.deleteById(999L);
        
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    void testDeleteAll() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);
        productRepository.save(product2);

        productRepository.deleteAll();

        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }

    @Test
    void testSaveMultipleProducts() {
        Product product1 = new Product();
        product1.setName("Product 1");
        product1.setDescription("Description 1");
        product1.setPrice(10.0);

        Product product2 = new Product();
        product2.setName("Product 2");
        product2.setDescription("Description 2");
        product2.setPrice(20.0);

        Product product3 = new Product();
        product3.setName("Product 3");
        product3.setDescription("Description 3");
        product3.setPrice(30.0);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        List<Product> products = productRepository.findAll();
        assertEquals(3, products.size());
    }

    @Test
    void testProductWithZeroPrice() {
        Product product = new Product();
        product.setName("Free Product");
        product.setDescription("Free item");
        product.setPrice(0.0);

        Product savedProduct = productRepository.save(product);

        assertEquals(0.0, savedProduct.getPrice(), 0.001);
    }

    @Test
    void testProductWithNegativePrice() {
        Product product = new Product();
        product.setName("Negative Price Product");
        product.setDescription("Product with negative price");
        product.setPrice(-10.0);

        Product savedProduct = productRepository.save(product);

        assertEquals(-10.0, savedProduct.getPrice(), 0.001);
    }

    @Test
    void testProductWithLargePrice() {
        Product product = new Product();
        product.setName("Expensive Product");
        product.setDescription("Very expensive item");
        product.setPrice(999999.99);

        Product savedProduct = productRepository.save(product);

        assertEquals(999999.99, savedProduct.getPrice(), 0.001);
    }

    @Test
    void testProductWithLongName() {
        String longName = "This is a very long product name that exceeds normal length limits to test how the system handles long strings in the database";
        Product product = new Product();
        product.setName(longName);
        product.setDescription("Test description");
        product.setPrice(50.0);

        Product savedProduct = productRepository.save(product);

        assertEquals(longName, savedProduct.getName());
    }

    @Test
    void testProductWithLongDescription() {
        String longDescription = "This is a very long product description that contains many words and details about the product features, specifications, and benefits. It should be long enough to test how the database handles large text fields.";
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription(longDescription);
        product.setPrice(50.0);

        Product savedProduct = productRepository.save(product);

        assertEquals(longDescription, savedProduct.getDescription());
    }
}
