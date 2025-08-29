package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void testListProducts() throws Exception {
        mockMvc.perform(get("/web/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void testCreateProduct() throws Exception {
        mockMvc.perform(post("/web/products")
                .param("name", "Test Product")
                .param("description", "Test Description")
                .param("price", "99.99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"));
    }

    @Test
    void testEditProduct() throws Exception {
        // Create a test product first
        Product product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(99.99);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/web/products/edit/" + savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void testEditProduct_NonExistingProduct() throws Exception {
        mockMvc.perform(get("/web/products/edit/999"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Create a test product first
        Product product = new Product();
        product.setName("Original Name");
        product.setDescription("Original Description");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(post("/web/products/update/" + savedProduct.getId())
                .param("name", "Updated Name")
                .param("description", "Updated Description")
                .param("price", "100.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    void testUpdateProduct_NonExistingProduct() throws Exception {
        mockMvc.perform(post("/web/products/update/999")
                .param("name", "Updated Name")
                .param("description", "Updated Description")
                .param("price", "100.0"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Create a test product first
        Product product = new Product();
        product.setName("To Delete");
        product.setDescription("Will be deleted");
        product.setPrice(50.0);
        Product savedProduct = productRepository.save(product);

        mockMvc.perform(get("/web/products/delete/" + savedProduct.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"))
                .andExpect(flash().attributeExists("successMessage"));

        // Verify product was deleted
        assertFalse(productRepository.findById(savedProduct.getId()).isPresent());
    }

    @Test
    void testDeleteProduct_NonExistingProduct() throws Exception {
        mockMvc.perform(get("/web/products/delete/999"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"));
    }

    @Test
    void testCreateProduct_WithEmptyFields() throws Exception {
        mockMvc.perform(post("/web/products")
                .param("name", "")
                .param("description", "")
                .param("price", "0.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"));
    }

    @Test
    void testCreateProduct_WithNullPrice() throws Exception {
        mockMvc.perform(post("/web/products")
                .param("name", "Test Product")
                .param("description", "Test Description")
                .param("price", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/web/products"));
    }

    @Test
    void testListProducts_WithMultipleProducts() throws Exception {
        // Create multiple test products
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

        mockMvc.perform(get("/web/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("product"));
    }
} 