package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    // Duplicate business logic - security hotspot
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    // Duplicate business logic - security hotspot (same as above)
    public List<Product> retrieveAllProducts() {
        return productRepository.findAll();
    }
    
    // Duplicate business logic - security hotspot
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    
    // Duplicate business logic - security hotspot (same as above)
    public Optional<Product> findProductById(Long productId) {
        return productRepository.findById(productId);
    }
    
    // Duplicate business logic - security hotspot
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    // Duplicate business logic - security hotspot (same as above)
    public Product createProduct(Product newProduct) {
        return productRepository.save(newProduct);
    }
    
    // Duplicate business logic - security hotspot
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
    
    // Duplicate business logic - security hotspot (same as above)
    public void removeProduct(Long productId) {
        productRepository.deleteById(productId);
    }
    
    // Duplicate validation logic - security hotspot
    public boolean isValidProduct(Product product) {
        return product.getName() != null && 
               !product.getName().trim().isEmpty() && 
               product.getPrice() > 0.0;
    }
    
    // Duplicate validation logic - security hotspot (same as above)
    public boolean validateProduct(Product productToValidate) {
        return productToValidate.getName() != null && 
               !productToValidate.getName().trim().isEmpty() && 
               productToValidate.getPrice() > 0.0;
    }
}
