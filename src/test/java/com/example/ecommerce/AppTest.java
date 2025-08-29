package com.example.ecommerce;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AppTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void testCreateProduct() {
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
    void testGetAllProducts() {
        // Create test products
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

        // Test REST API
        String url = "http://localhost:" + port + "/products";
        Product[] products = restTemplate.getForObject(url, Product[].class);

        assertNotNull(products);
        assertEquals(2, products.length);
    }

    @Test
    void testGetProductById() {
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        Product savedProduct = productRepository.save(product);

        String url = "http://localhost:" + port + "/products/" + savedProduct.getId();
        Product retrievedProduct = restTemplate.getForObject(url, Product.class);

        assertNotNull(retrievedProduct);
        assertEquals(savedProduct.getId(), retrievedProduct.getId());
        assertEquals("Test Product", retrievedProduct.getName());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Original Name");
        product.setDescription("Original Description");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        Product updateData = new Product();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        updateData.setPrice(100.0);

        String url = "http://localhost:" + port + "/products/" + savedProduct.getId();
        restTemplate.put(url, updateData);

        Product updatedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertNotNull(updatedProduct);
        assertEquals("Updated Name", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(100.0, updatedProduct.getPrice(), 0.001);
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product();
        product.setName("To Delete");
        product.setDescription("Will be deleted");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        String url = "http://localhost:" + port + "/products/" + savedProduct.getId();
        restTemplate.delete(url);

        assertFalse(productRepository.findById(savedProduct.getId()).isPresent());
    }

    @Test
    void testWebEndpoints() {
        // Test home page redirect
        String homeUrl = "http://localhost:" + port + "/";
        String response = restTemplate.getForObject(homeUrl, String.class);
        assertNotNull(response);

        // Test products web page
        String productsUrl = "http://localhost:" + port + "/web/products";
        String productsResponse = restTemplate.getForObject(productsUrl, String.class);
        assertNotNull(productsResponse);
    }
}
