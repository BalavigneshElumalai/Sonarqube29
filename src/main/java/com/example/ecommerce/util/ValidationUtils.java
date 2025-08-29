package com.example.ecommerce.util;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    // Duplicate validation method - security hotspot
    public static boolean isValidProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        if (name.length() > 100) {
            return false;
        }
        // Duplicate regex pattern - security hotspot
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s\\-_]+$");
        return pattern.matcher(name).matches();
    }
    
    // Duplicate validation method - security hotspot (same logic as above)
    public static boolean validateProductName(String productName) {
        if (productName == null || productName.trim().isEmpty()) {
            return false;
        }
        if (productName.length() > 100) {
            return false;
        }
        // Duplicate regex pattern - security hotspot
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\s\\-_]+$");
        return pattern.matcher(productName).matches();
    }
    
    // Duplicate price validation - security hotspot
    public static boolean isValidPrice(double price) {
        return price > 0.0 && price <= 999999.99;
    }
    
    // Duplicate price validation - security hotspot (same logic as above)
    public static boolean validateProductPrice(double productPrice) {
        return productPrice > 0.0 && productPrice <= 999999.99;
    }
    
    // Duplicate string sanitization - security hotspot
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.replaceAll("<script>", "")
                   .replaceAll("</script>", "")
                   .replaceAll("javascript:", "")
                   .trim();
    }
    
    // Duplicate string sanitization - security hotspot (same logic as above)
    public static String cleanUserInput(String userInput) {
        if (userInput == null) {
            return "";
        }
        return userInput.replaceAll("<script>", "")
                       .replaceAll("</script>", "")
                       .replaceAll("javascript:", "")
                       .trim();
    }
}
