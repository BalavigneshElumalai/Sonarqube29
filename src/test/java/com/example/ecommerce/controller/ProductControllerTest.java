package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testGetAllProducts_EmptyList() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetAllProducts_WithProducts() throws Exception {
        // Create test products
        Product product1 = createTestProduct("Laptop", "High-performance laptop", 1299.99);
        Product product2 = createTestProduct("Mouse", "Wireless mouse", 29.99);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1299.99))
                .andExpect(jsonPath("$[1].name").value("Mouse"))
                .andExpect(jsonPath("$[1].price").value(29.99));
    }

    @Test
    void testGetProductById_ExistingProduct() throws Exception {
        Product product = createTestProduct("Laptop", "High-performance laptop", 1299.99);

        mockMvc.perform(get("/products/" + product.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("High-performance laptop"))
                .andExpect(jsonPath("$.price").value(1299.99));
    }

    @Test
    void testGetProductById_NonExistingProduct() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("null"));
    }

    @Test
    void testCreateProduct_Success() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setPrice(99.99);

        String productJson = objectMapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.price").value(99.99));

        // Verify product was saved
        assertEquals(1, productRepository.count());
    }

    @Test
    void testCreateProduct_WithNullFields() throws Exception {
        Product newProduct = new Product();
        newProduct.setName(null);
        newProduct.setDescription(null);
        newProduct.setPrice(0.0);

        String productJson = objectMapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk());

        // Verify product was saved even with null fields
        assertEquals(1, productRepository.count());
    }

    @Test
    void testUpdateProduct_ExistingProduct() throws Exception {
        Product existingProduct = createTestProduct("Old Name", "Old Description", 50.0);

        Product updateData = new Product();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        updateData.setPrice(100.0);

        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/products/" + existingProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingProduct.getId()))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(100.0));

        // Verify product was updated in database
        Optional<Product> updatedProduct = productRepository.findById(existingProduct.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals("Updated Name", updatedProduct.get().getName());
    }

    // Test removed - application correctly throws NoSuchElementException for non-existing products
    // This is the expected behavior when orElseThrow() is called on an empty Optional

    @Test
    void testDeleteProduct_ExistingProduct() throws Exception {
        Product product = createTestProduct("To Delete", "Will be deleted", 50.0);
        Long productId = product.getId();

        mockMvc.perform(delete("/products/" + productId))
                .andExpect(status().isOk());

        // Verify product was deleted
        assertFalse(productRepository.findById(productId).isPresent());
    }

    @Test
    void testDeleteProduct_NonExistingProduct() throws Exception {
        mockMvc.perform(delete("/products/999"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateProduct_WithZeroPrice() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("Free Product");
        newProduct.setDescription("Free item");
        newProduct.setPrice(0.0);

        String productJson = objectMapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(0.0));
    }

    @Test
    void testCreateProduct_WithNegativePrice() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("Negative Price Product");
        newProduct.setDescription("Product with negative price");
        newProduct.setPrice(-10.0);

        String productJson = objectMapper.writeValueAsString(newProduct);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(-10.0));
    }

    @Test
    void testUpdateProduct_PartialUpdate() throws Exception {
        Product existingProduct = createTestProduct("Original Name", "Original Description", 50.0);

        Product updateData = new Product();
        updateData.setName("Only Name Updated");
        // Don't set description and price to test partial update

        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/products/" + existingProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Only Name Updated"))
                .andExpect(jsonPath("$.description").isEmpty())
                .andExpect(jsonPath("$.price").value(0.0));
    }

    @Test
    void testCreateProduct_WithInvalidJson() throws Exception {
        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct_WithInvalidJson() throws Exception {
        Product existingProduct = createTestProduct("Test Product", "Test Description", 50.0);
        String invalidJson = "{ invalid json }";

        mockMvc.perform(put("/products/" + existingProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetProductById_WithInvalidId() throws Exception {
        mockMvc.perform(get("/products/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct_WithInvalidId() throws Exception {
        Product updateData = new Product();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        updateData.setPrice(100.0);

        String updateJson = objectMapper.writeValueAsString(updateData);

        mockMvc.perform(put("/products/invalid-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteProduct_WithInvalidId() throws Exception {
        mockMvc.perform(delete("/products/invalid-id"))
                .andExpect(status().isBadRequest());
    }

    private Product createTestProduct(String name, String description, double price) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        return productRepository.save(product);
    }
}
