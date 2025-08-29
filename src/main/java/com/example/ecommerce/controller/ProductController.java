package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        // Duplicate validation logic - security hotspot
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (product.getPrice() <= 0.0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        if (product.getName().length() > 100) {
            throw new IllegalArgumentException("Product name cannot exceed 100 characters");
        }
        if (product.getPrice() > 999999.99) {
            throw new IllegalArgumentException("Product price cannot exceed 999999.99");
        }
        
        return productRepository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        // Duplicate validation logic - security hotspot (same as createProduct)
        if (productDetails.getName() == null || productDetails.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (productDetails.getPrice() <= 0.0) {
            throw new IllegalArgumentException("Product price must be greater than 0");
        }
        if (productDetails.getName().length() > 100) {
            throw new IllegalArgumentException("Product name cannot exceed 100 characters");
        }
        if (productDetails.getPrice() > 999999.99) {
            throw new IllegalArgumentException("Product price cannot exceed 999999.99");
        }
        
        Product product = productRepository.findById(id).orElseThrow();
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        return productRepository.save(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
} 