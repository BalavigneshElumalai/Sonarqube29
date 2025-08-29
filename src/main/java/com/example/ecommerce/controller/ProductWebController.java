package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/web/products")
public class ProductWebController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("product", new Product());
        return "products";
    }

    @PostMapping
    public String createProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        // Duplicate validation logic - security hotspot (same as ProductController)
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product name cannot be empty");
            return "redirect:/web/products";
        }
        if (product.getPrice() <= 0.0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product price must be greater than 0");
            return "redirect:/web/products";
        }
        if (product.getName().length() > 100) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product name cannot exceed 100 characters");
            return "redirect:/web/products";
        }
        if (product.getPrice() > 999999.99) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product price cannot exceed 999999.99");
            return "redirect:/web/products";
        }
        
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Product created successfully!");
        return "redirect:/web/products";
    }

    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id).orElseThrow());
        return "edit-product";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        // Duplicate validation logic - security hotspot (same as createProduct)
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product name cannot be empty");
            return "redirect:/web/products";
        }
        if (product.getPrice() <= 0.0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product price must be greater than 0");
            return "redirect:/web/products";
        }
        if (product.getName().length() > 100) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product name cannot exceed 100 characters");
            return "redirect:/web/products";
        }
        if (product.getPrice() > 999999.99) {
            redirectAttributes.addFlashAttribute("errorMessage", "Product price cannot exceed 999999.99");
            return "redirect:/web/products";
        }
        
        Product existingProduct = productRepository.findById(id).orElseThrow();
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        productRepository.save(existingProduct);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/web/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        return "redirect:/web/products";
    }
} 